package kr.hhplus.be.server.domain.order.info;

import kr.hhplus.be.server.api.order.response.OrderResponse;
import kr.hhplus.be.server.domain.coupon.info.CouponInfo;
import kr.hhplus.be.server.infra.order.entity.Order.OrderShipStatus;
import kr.hhplus.be.server.infra.order.entity.Order.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Objects;

import static kr.hhplus.be.server.infra.order.entity.Order.OrderStatus.PAYED;

@Data
@Builder
public class OrderInfo {
    private Long id;
    private Long finalPrice;
    private Long originalPrice;
    private OrderStatus orderStatus;
    private OrderShipStatus orderShipStatus;
    private List<OrderDetailInfo> orderDetails;

    public void applyCoupon(final CouponInfo couponInfo) {
        finalPrice = originalPrice - originalPrice * couponInfo.getRate() / 100;
    }

    public void orderPayDone() {
        orderStatus = PAYED;
    }

    public Long getFinalPrice() {
        return Objects.isNull(finalPrice)
             ? originalPrice
             : finalPrice;
    }

    public OrderResponse toResponse() {
        return new OrderResponse(
            id, finalPrice, originalPrice, orderStatus, orderShipStatus,
            orderDetails.stream()
                        .map(OrderDetailInfo::toResponse)
                        .toList()
        );
    }
}
