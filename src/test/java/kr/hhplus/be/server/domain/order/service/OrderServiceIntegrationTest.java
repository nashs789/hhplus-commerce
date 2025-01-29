package kr.hhplus.be.server.domain.order.service;

import kr.hhplus.be.server.domain.member.info.CartProductInfo;
import kr.hhplus.be.server.domain.member.service.MemberService;
import kr.hhplus.be.server.domain.order.info.OrderDetailInfo;
import kr.hhplus.be.server.domain.order.info.OrderInfo;
import kr.hhplus.be.server.domain.product.info.ProductInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static kr.hhplus.be.server.infra.order.entity.Order.OrderShipStatus.NOT_DEPARTURE;
import static kr.hhplus.be.server.infra.order.entity.Order.OrderStatus.NOT_PAYED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Sql(
        scripts = {
                "/test-member-data.sql",
                "/test-cart-data.sql",
                "/test-product-data.sql",
                "/test-cart-product-data.sql",
                "/test-product-inventory-data.sql"
        },
        config = @SqlConfig(encoding = "UTF-8")
)
@SpringBootTest
@Testcontainers
class OrderServiceIntegrationTest {

    final Long MEMBER_ID = 1L;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("오더 정상 생성")
    void createOrder(){
        // given
        final List<CartProductInfo> products = memberService.findCartProductsByMemberId(MEMBER_ID);
        final List<CartProductInfo> productsInCart = products.stream()
                                                             .map(e -> CartProductInfo.builder()
                                                                                      .productInfo(e.getProductInfo())
                                                                                      .quantity(e.getQuantity())
                                                                                      .build()
                                                             )
                                                             .toList();
        final Long ORIGINAL_PRICE = products.stream()
                                            .mapToLong(e -> e.getProductInfo().getPrice() * e.getQuantity())
                                            .sum();
        final List<String> names = products.stream()
                                           .map(CartProductInfo::getProductInfo)
                                           .map(ProductInfo::getName)
                                           .toList();

        // when
        OrderInfo result = orderService.createOrder(productsInCart, 1L);

        // then
        assertAll(() -> {
            assertEquals(ORIGINAL_PRICE, result.getOriginalPrice());
            assertEquals(NOT_PAYED, result.getOrderStatus());
            assertEquals(NOT_DEPARTURE, result.getOrderShipStatus());
            assertEquals(products.size(), result.getOrderDetails().size());
            assertThat(result.getOrderDetails()
                             .stream()
                             .map(OrderDetailInfo::getProductInfo)
                             .map(ProductInfo::getName)
                             .toList())
                    .containsExactlyInAnyOrderElementsOf(names);
        });
    }
}