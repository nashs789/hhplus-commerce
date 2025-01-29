package kr.hhplus.be.server.application.payment.facade;

import kr.hhplus.be.server.domain.payment.info.PaymentInfo;
import kr.hhplus.be.server.infra.coupon.repository.CouponJpaRepository;
import kr.hhplus.be.server.infra.order.repository.OrderJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.testcontainers.junit.jupiter.Testcontainers;

import static kr.hhplus.be.server.infra.order.entity.Order.OrderStatus.PAYED;
import static kr.hhplus.be.server.infra.payment.entity.Payment.PaymentStatus.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Sql(
        scripts = {
                "/test-member-data.sql",
                "/test-coupon-data.sql",
                "/test-coupon-history-data.sql",
                "/test-cart-data.sql",
                "/test-product-data.sql",
                "/test-product-inventory-data.sql",
                "/test-cart-product-data.sql",
                "/test-order-data.sql",
                "/test-order-detail-data.sql"
        },
        config = @SqlConfig(encoding = "UTF-8")
)
@SpringBootTest
@Testcontainers
class PaymentFacadeTest {

    final Long MEMBER_ID = 1L;
    final Long ORDER_ID = 1L;
    final Long COUPON_ID = 1L;

    @Autowired
    private PaymentFacade paymentFacade;

    @Autowired
    private OrderJpaRepository orderJpaRepository;

    @Autowired
    private CouponJpaRepository couponJpaRepository;

    @Test
    @DisplayName("결제 진행")
    void successPayment() {
        // when & given
        PaymentInfo paymentInfo = paymentFacade.paymentProgress(MEMBER_ID, ORDER_ID, null);

        // then
        final Long FINAL_PRICE = orderJpaRepository.findById(ORDER_ID)
                                                   .orElseThrow(IllegalArgumentException::new)
                                                   .getOriginalPrice();

        assertAll(() -> {
            assertEquals(SUCCESS , paymentInfo.getPaymentStatus());
            assertEquals(PAYED , paymentInfo.getOrderInfo().getOrderStatus());
            assertEquals(FINAL_PRICE, paymentInfo.getAmount());
        });
    }

    @Test
    @DisplayName("쿠폰 결제 진행")
    void successPaymentWithCoupon() {
        // when & given
        PaymentInfo paymentInfo = paymentFacade.paymentProgress(MEMBER_ID, ORDER_ID, COUPON_ID);

        // then
        final Long ORDER_PRICE = orderJpaRepository.findById(ORDER_ID)
                                                   .orElseThrow(IllegalArgumentException::new)
                                                   .getOriginalPrice();
        final Integer rate = couponJpaRepository.findById(COUPON_ID)
                                                .orElseThrow(IllegalArgumentException::new)
                                                .getRate();
        final Long FINAL_PRICE = ORDER_PRICE - (ORDER_PRICE * rate / 100);  // 할인율 계산

        assertAll(() -> {
            assertEquals(SUCCESS , paymentInfo.getPaymentStatus());
            assertEquals(PAYED , paymentInfo.getOrderInfo().getOrderStatus());
            assertEquals(FINAL_PRICE, paymentInfo.getAmount());
        });
    }
}