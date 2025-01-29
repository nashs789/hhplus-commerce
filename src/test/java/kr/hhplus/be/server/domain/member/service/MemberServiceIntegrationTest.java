package kr.hhplus.be.server.domain.member.service;

import kr.hhplus.be.server.domain.member.command.CartAddCommand;
import kr.hhplus.be.server.domain.member.command.CartDeleteCommand;
import kr.hhplus.be.server.domain.member.command.PointChargeCommand;
import kr.hhplus.be.server.domain.member.command.PointUseCommand;
import kr.hhplus.be.server.domain.member.exception.PointException;
import kr.hhplus.be.server.domain.member.info.CartProductInfo;
import kr.hhplus.be.server.domain.member.info.MemberInfo;
import kr.hhplus.be.server.domain.product.info.ProductInfo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static kr.hhplus.be.server.infra.member.entity.PointHistory.PointUseType.CHARGE;
import static kr.hhplus.be.server.infra.member.entity.PointHistory.PointUseType.USE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Sql(
        scripts = {
                "/test-member-data.sql",
                "/test-cart-data.sql",
                "/test-product-data.sql",
                "/test-cart-product-data.sql"
        },
        config = @SqlConfig(encoding = "UTF-8")
)
@SpringBootTest
@Testcontainers
class MemberServiceIntegrationTest {

    final Long MEMBER_ID = 1L;

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("멤버 조회")
    void findMemberById() {
        // given & when
        MemberInfo result = memberService.findMemberById(MEMBER_ID);

        // then
        assertEquals(MEMBER_ID, result.getId());
    }

    @Test
    @DisplayName("동시 충전 동시성 테스트")
    void chargeMemberPointConcurrencyTest() throws InterruptedException {
        // given
        final int TEST_CNT = 30;
        final long CHARGE_POINT = 10_000L;
        final Long BASE_POINT = memberService.findMemberById(MEMBER_ID).getPoint();
        ExecutorService executor = Executors.newFixedThreadPool(30);
        CountDownLatch latch = new CountDownLatch(TEST_CNT);

        // when
        for(int i = 0; i < TEST_CNT; i++) {
            executor.submit(() -> {
                try {
                    memberService.chargeMemberPoint(PointChargeCommand.builder()
                                                                      .memberId(MEMBER_ID)
                                                                      .chargePoint(CHARGE_POINT)
                                                                      .pointUseType(CHARGE)
                                                                      .build());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        MemberInfo result = memberService.findMemberById(MEMBER_ID);

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
        final Long BASE_POINT = memberService.findMemberById(MEMBER_ID).getPoint();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(TEST_CNT);

        // when
        for(int i = 0; i < TEST_CNT; i++) {
            executor.submit(() -> {
                try {
                    memberService.useMemberPoint(PointUseCommand.builder()
                                                                .memberId(MEMBER_ID)
                                                                .usePoint(USE_POINT)
                                                                .pointUseType(USE)
                                                                .build());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        MemberInfo result = memberService.findMemberById(MEMBER_ID);
        final Long FINAL_POINT = BASE_POINT - (TEST_CNT * USE_POINT);

        // then
        assertEquals(0L, result.getPoint());
        assertEquals(FINAL_POINT, latch.getCount());
    }

    @Test
    @DisplayName("포인트 사용 실패")
    void failUseMemberPoint() {
        // given
        final Long BASE_POINT = memberService.findMemberById(MEMBER_ID).getPoint();
        final Long USE_POINT = BASE_POINT + 1L;

        // when
        PointException pointException = assertThrows(PointException.class, () ->
                memberService.useMemberPoint(PointUseCommand.builder()
                                                            .memberId(MEMBER_ID)
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
        List<CartProductInfo> result = memberService.findCartProductsByMemberId(MEMBER_ID);

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
        final Long NEW_PRODUCT_ID = 3L;
        final List<CartAddCommand> cartAddCommands = List.of(CartAddCommand.builder()
                                                                           .productId(NEW_PRODUCT_ID)
                                                                           .cnt(5L)
                                                                           .build());

        // when
        List<CartProductInfo> result = memberService.addCartByProductId(MEMBER_ID, cartAddCommands);

        // then
        assertEquals(cartAddCommands.size(), result.size());
        assertEquals(NEW_PRODUCT_ID, result.get(0).getProductInfo().getId());
    }

    @Test
    @DisplayName("장바구니 상품 삭제")
    void deleteProductFromCart() {
        // given
        List<Long> idList = memberService.findCartProductsByMemberId(1L)
                                         .stream()
                                         .map(CartProductInfo::getId)
                                         .toList();
        List<CartDeleteCommand> deleteCommands = idList.stream()
                                                       .map(e -> CartDeleteCommand.builder()
                                                                                  .cartProductId(e)
                                                                                  .build()
                                                       )
                                                       .toList();

        // when
        memberService.deleteCartByProductId(deleteCommands);
        List<CartProductInfo> result = memberService.findCartProductsByMemberId(MEMBER_ID);

        // then
        assertEquals(0, result.size());
    }
}