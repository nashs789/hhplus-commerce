package kr.hhplus.be.server.domain.order.info;

import kr.hhplus.be.server.api.order.response.OrderResponse;
import kr.hhplus.be.server.infra.order.entity.Order.OrderShipStatus;
import kr.hhplus.be.server.infra.order.entity.Order.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderInfo {
    private Long id;
    private Long finalPrice;
    private Long originalPrice;
    private OrderStatus orderStatus;
    private OrderShipStatus orderShipStatus;
    private List<OrderDetailInfo> orderDetails;

    public OrderResponse toResponse() {
        return new OrderResponse(
            id, finalPrice, originalPrice, orderStatus, orderShipStatus,
            orderDetails.stream()
                        .map(OrderDetailInfo::toResponse)
                        .toList()
        );
    }
}
