package kr.hhplus.be.server.domain.member.service;

import kr.hhplus.be.server.domain.member.command.CartAddCommand;
import kr.hhplus.be.server.domain.member.command.CartDeleteCommand;
import kr.hhplus.be.server.domain.member.command.PointChargeCommand;
import kr.hhplus.be.server.domain.member.command.PointUseCommand;
import kr.hhplus.be.server.domain.member.exception.PointException;
import kr.hhplus.be.server.domain.member.info.CartProductInfo;
import kr.hhplus.be.server.domain.member.info.MemberInfo;
import kr.hhplus.be.server.domain.product.info.ProductInfo;
import kr.hhplus.be.server.infra.member.entity.Cart;
import kr.hhplus.be.server.infra.member.entity.CartProduct;
import kr.hhplus.be.server.infra.member.entity.Member;
import kr.hhplus.be.server.infra.member.repository.CartJpaRepository;
import kr.hhplus.be.server.infra.member.repository.CartProductJpaRepository;
import kr.hhplus.be.server.infra.member.repository.MemberJpaRepository;
import kr.hhplus.be.server.infra.product.entity.Product;
import kr.hhplus.be.server.infra.product.repository.ProductJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static kr.hhplus.be.server.infra.member.entity.PointHistory.PointUseType.CHARGE;
import static kr.hhplus.be.server.infra.member.entity.PointHistory.PointUseType.USE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@SpringBootTest
@Testcontainers
class MemberServiceIntegrationTest {

    private final Long BASE_POINT = 100_000L;
    private Member member;
    private Cart cart;
    private Product product;
    private List<CartProduct> cartProducts = new ArrayList<>();

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private CartJpaRepository cartJpaRepository;

    @Autowired
    private CartProductJpaRepository cartProductJpaRepository;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @BeforeEach
    void setUp() {
        member = memberJpaRepository.save(Member.builder()
                                                .point(BASE_POINT)
                                                .build());
        cart = cartJpaRepository.save(Cart.builder()
                                               .member(member)
                                               .build());
        product = productJpaRepository.save(Product.builder()
                                                   .name("테스트 상품")
                                                   .price(5_000L)
                                                   .build());
        productJpaRepository.save(product);

        List<String> names = List.of("상품 A", "상품 B");

        for(int i = 0; i < names.size(); i++) {
            Product productInCart = productJpaRepository.save(Product.builder()
                                                        .name(names.get(i))
                                                        .price(1_000L * i)
                                                        .build());

            cartProducts.add(
                    cartProductJpaRepository.save(CartProduct.builder()
                                                             .cart(cart)
                                                             .quantity(10L)
                                                             .product(productInCart)
                                                             .build()
                    )
            );
        }
    }

    @Test
    @DisplayName("멤버 조회")
    void findMemberById() {
        // given & when
        MemberInfo result = memberService.findMemberById(member.getId());

        // then
        assertEquals(member.getId(), result.getId());
    }

    @Test
    @DisplayName("동시 충전 동시성 테스트")
    void chargeMemberPointConcurrencyTest() throws InterruptedException {
        // given
        final int TEST_CNT = 30;
        final long CHARGE_POINT = 10_000L;
        ExecutorService executor = Executors.newFixedThreadPool(30);
        CountDownLatch latch = new CountDownLatch(TEST_CNT);

        // when
        for(int i = 0; i < TEST_CNT; i++) {
            executor.submit(() -> {
                try {
                    memberService.chargeMemberPoint(PointChargeCommand.builder()
                                                                      .memberId(member.getId())
                                                                      .chargePoint(CHARGE_POINT)
                                                                      .pointUseType(CHARGE)
                                                                      .build());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        MemberInfo result = memberService.findMemberById(member.getId());

        // then
        final Long FINAL_POINT = BASE_POINT + TEST_CNT * CHARGE_POINT;

        assertEquals(FINAL_POINT, result.getPoint());
        assertEquals(0, latch.getCount());
    }

    @Test
    @DisplayName("동시 사용 동시성 테스트")
    void useMemberPointConcurrencyTest() throws InterruptedException {
        // given
        final int TEST_CNT = 10;
        final long USE_POINT = 10_000L;
        ExecutorService executor = Executors.newFixedThreadPool(30);
        CountDownLatch latch = new CountDownLatch(TEST_CNT);

        // when
        for(int i = 0; i < TEST_CNT; i++) {
            executor.submit(() -> {
                try {
                    memberService.useMemberPoint(PointUseCommand.builder()
                                                                .memberId(member.getId())
                                                                .usePoint(USE_POINT)
                                                                .pointUseType(USE)
                                                                .build());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        MemberInfo result = memberService.findMemberById(member.getId());

        // then
        assertEquals(0L, result.getPoint());
        assertEquals(0, latch.getCount());
    }

    @Test
    @DisplayName("포인트 사용 실패")
    void failUseMemberPoint() {
        // given
        final Long USE_POINT = BASE_POINT + 1L;

        // when
        PointException pointException = assertThrows(PointException.class, () ->
                memberService.useMemberPoint(PointUseCommand.builder()
                                                            .memberId(member.getId())
                                                            .usePoint(USE_POINT)
                                                            .pointUseType(USE)
                                                            .build())
        );

        // then
        assertEquals(PointException.class, pointException.getClass());
        assertEquals(BAD_REQUEST, pointException.getStatus());
        assertEquals("포인트가 부족 합니다.", pointException.getMessage());
    }

    @Test
    @DisplayName("장바구니 등록 상품 조회")
    void findCartByMemberId() {
        // given & when
        List<CartProductInfo> result = memberService.findCartProductsByMemberId(member.getId());

        // then
        assertEquals(2, result.size());
        Assertions.assertThat(result.stream()
                                    .map(CartProductInfo::getProductInfo)
                                    .map(ProductInfo::getName)
                                    .toList())
                  .containsExactlyInAnyOrderElementsOf(List.of("상품 A", "상품 B"));
    }

    @Test
    @DisplayName("장바구니 상품 추가")
    void addProductToCart() {
        // given
        final List<CartAddCommand> cartAddCommands = List.of(CartAddCommand.builder()
                                                                           .productId(product.getId())
                                                                           .cnt(5L)
                                                                           .build());

        // when
        List<CartProductInfo> result = memberService.addCartByProductId(member.getId(), cartAddCommands);

        System.out.println(result.get(0));

        // then
        assertEquals(1, result.size());
        assertEquals(product.getId(), result.get(0).getProductInfo().getId());
    }

    @Test
    @DisplayName("장바구니 상품 삭제")
    void deleteProductFromCart() {
        // given
        List<CartDeleteCommand> deleteCommands = cartProducts.stream()
                                                             .map(e -> CartDeleteCommand.builder()
                                                                                        .cartProductId(e.getId())
                                                                                        .build()
                                                             )
                                                             .toList();

        // when
        memberService.deleteCartByProductId(deleteCommands);
        List<CartProductInfo> result = memberService.findCartProductsByMemberId(member.getId());

        // then
        assertEquals(0, result.size());
    }
}