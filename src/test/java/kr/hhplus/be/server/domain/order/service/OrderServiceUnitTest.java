package kr.hhplus.be.server.domain.order.service;

import kr.hhplus.be.server.domain.member.info.CartProductInfo;
import kr.hhplus.be.server.domain.order.info.OrderDetailInfo;
import kr.hhplus.be.server.domain.order.info.OrderInfo;
import kr.hhplus.be.server.domain.order.repository.OrderRepository;
import kr.hhplus.be.server.domain.product.info.ProductInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static kr.hhplus.be.server.infra.order.entity.Order.OrderShipStatus.NOT_DEPARTURE;
import static kr.hhplus.be.server.infra.order.entity.Order.OrderStatus.NOT_PAYED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceUnitTest {

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    OrderService orderService;

    @Test
    @DisplayName("오더 생성")
    void createOrder() {
        final Long PRICE = 100L;
        final Long QUANTITY = 10L;
        // given
        ProductInfo productInfo = ProductInfo.builder()
                                             .price(PRICE)
                                             .build();
        List<CartProductInfo> productsInCart = List.of(
                CartProductInfo.builder()
                               .productInfo(productInfo)
                               .quantity(QUANTITY)
                               .build()
        );
        List<OrderDetailInfo> orderDetails = List.of(
                OrderDetailInfo.builder()
                               .productInfo(productInfo)
                               .quantity(QUANTITY)
                               .build()
        );
        OrderInfo orderInfo = OrderInfo.builder()
                                       .orderStatus(NOT_PAYED)
                                       .orderShipStatus(NOT_DEPARTURE)
                                       .orderDetails(orderDetails)
                                       .finalPrice(PRICE * QUANTITY)
                                       .build();
        when(orderRepository.createOrder(anyLong(), anyLong(), any())).thenReturn(orderInfo);

        // when
        OrderInfo result = orderService.createOrder(productsInCart, 1L);

        // then
        assertEquals(1, result.getOrderDetails().size());
        assertEquals(QUANTITY * PRICE, result.getFinalPrice());
        assertEquals(NOT_PAYED, result.getOrderStatus());
        assertEquals(NOT_DEPARTURE, result.getOrderShipStatus());
    }
}