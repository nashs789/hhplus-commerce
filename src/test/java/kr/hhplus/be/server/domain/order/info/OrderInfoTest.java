package kr.hhplus.be.server.domain.order.info;

import kr.hhplus.be.server.domain.coupon.info.CouponInfo;
import kr.hhplus.be.server.infra.order.entity.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kr.hhplus.be.server.infra.order.entity.Order.OrderStatus.NOT_PAYED;
import static kr.hhplus.be.server.infra.order.entity.Order.OrderStatus.PAYED;
import static org.junit.jupiter.api.Assertions.*;

class OrderInfoTest {

    @Test
    @DisplayName("오더에 쿠폰 가격 적용")
    void applyCoupon() {
        // given
        final OrderInfo orderInfo = OrderInfo.builder()
                                             .originalPrice(10_000L)
                                             .build();
        final CouponInfo couponInfo = CouponInfo.builder()
                                                .rate(10)
                                                .build();

        // when
        orderInfo.applyCoupon(couponInfo);

        // then
        assertEquals(9_000L, orderInfo.getFinalPrice());
    }

    @Test
    @DisplayName("오더 상태 결제 완료로 변경")
    void orderPayDone() {
        // given
        final OrderInfo orderInfo = OrderInfo.builder()
                                             .orderStatus(NOT_PAYED)
                                             .build();

        // when
        orderInfo.orderPayDone();

        // then
        assertEquals(PAYED, orderInfo.getOrderStatus());
    }
}