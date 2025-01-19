package kr.hhplus.be.server.domain.order.service;

import kr.hhplus.be.server.domain.member.info.CartProductInfo;
import kr.hhplus.be.server.domain.member.service.MemberService;
import kr.hhplus.be.server.domain.order.info.OrderDetailInfo;
import kr.hhplus.be.server.domain.order.info.OrderInfo;
import kr.hhplus.be.server.domain.product.info.ProductInfo;
import kr.hhplus.be.server.infra.member.entity.Cart;
import kr.hhplus.be.server.infra.member.entity.CartProduct;
import kr.hhplus.be.server.infra.member.entity.Member;
import kr.hhplus.be.server.infra.member.repository.CartJpaRepository;
import kr.hhplus.be.server.infra.member.repository.CartProductJpaRepository;
import kr.hhplus.be.server.infra.member.repository.MemberJpaRepository;
import kr.hhplus.be.server.infra.order.entity.Order;
import kr.hhplus.be.server.infra.product.entity.Product;
import kr.hhplus.be.server.infra.product.repository.ProductJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

import static kr.hhplus.be.server.infra.order.entity.Order.OrderShipStatus.NOT_DEPARTURE;
import static kr.hhplus.be.server.infra.order.entity.Order.OrderStatus.NOT_PAYED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class OrderServiceIntegrationTest {

    private final Long BASE_POINT = 100_000L;
    Member member;
    Cart cart;
    List<Product> products;
    List<CartProduct> cartProducts = new ArrayList<>();

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Autowired
    CartJpaRepository cartJpaRepository;

    @Autowired
    ProductJpaRepository productJpaRepository;

    @Autowired
    CartProductJpaRepository cartProductJpaRepository;

    @Autowired
    OrderService orderService;
    @Autowired
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        member = memberJpaRepository.save(Member.builder()
                                                .point(BASE_POINT)
                                                .build());
        cart = cartJpaRepository.save(Cart.builder()
                                          .member(member)
                                          .build());
        products = productJpaRepository.saveAll(List.of(
                Product.builder()
                       .name("테스트 상품A")
                       .price(5_000L)
                       .build(),
                Product.builder()
                       .name("테스트 상품B")
                       .price(10_000L)
                       .build(),
                Product.builder()
                       .name("테스트 상품C")
                       .price(50_000L)
                       .build()
        ));

        for(Product product : products) {
            cartProducts.add(cartProductJpaRepository.save(CartProduct.builder()
                                                                      .quantity(10L)
                                                                      .cart(cart)
                                                                      .product(product)
                                                                      .build()));
        }
    }

    @Test
    @DisplayName("오더 정상 생성")
    void test(){
        // given
        final List<CartProductInfo> productsInCart = cartProducts.stream()
                                                                 .map(e -> CartProductInfo.builder()
                                                                         .productInfo(e.getProduct().toInfo())
                                                                         .quantity(e.getQuantity())
                                                                         .build()
                                                                 )
                                                                 .toList();

        // when
        OrderInfo result = orderService.createOrder(productsInCart, member.getId());

        // then
        assertAll(() -> {
            assertEquals(650_000L, result.getOriginalPrice());
            assertEquals(NOT_PAYED, result.getOrderStatus());
            assertEquals(NOT_DEPARTURE, result.getOrderShipStatus());
            assertEquals(3, result.getOrderDetails().size());
            assertThat(result.getOrderDetails()
                             .stream()
                             .map(OrderDetailInfo::getProductInfo)
                             .map(ProductInfo::getName)
                             .toList())
                    .containsExactlyInAnyOrderElementsOf(List.of("테스트 상품A", "테스트 상품B", "테스트 상품C"));
        });
    }
}