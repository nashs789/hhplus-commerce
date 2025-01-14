package kr.hhplus.be.server.api.order.response;

import kr.hhplus.be.server.infra.order.entity.Order.OrderShipStatus;
import kr.hhplus.be.server.infra.order.entity.Order.OrderStatus;

import java.util.List;

public record OrderResponse(
        Long id,
        Long finalPrice,
        Long originalPrice,
        OrderStatus orderStatus,
        OrderShipStatus orderShipStatus,
        List<OrderDetailResponse> orderDetails
) {
}
