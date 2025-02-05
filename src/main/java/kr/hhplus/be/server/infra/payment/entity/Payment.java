package kr.hhplus.be.server.infra.payment.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.order.info.OrderInfo;
import kr.hhplus.be.server.domain.payment.info.PaymentInfo;
import kr.hhplus.be.server.infra.common.entity.Timestamp;
import kr.hhplus.be.server.infra.coupon.entity.Coupon;
import kr.hhplus.be.server.infra.order.entity.Order;
import lombok.*;
import lombok.experimental.SuperBuilder;

import static kr.hhplus.be.server.infra.payment.entity.Payment.PaymentMethod.CASH;
import static kr.hhplus.be.server.infra.payment.entity.Payment.PaymentStatus.FAIL;
import static kr.hhplus.be.server.infra.payment.entity.Payment.PaymentStatus.SUCCESS;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table
public class Payment extends Timestamp {

    @Getter
    @RequiredArgsConstructor
    public enum PaymentStatus {
        SUCCESS,
        FAIL
    }

    @Getter
    @RequiredArgsConstructor
    public enum PaymentMethod {
        CASH,
        CARD
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column
    private Long amount;

    public static Payment from(final OrderInfo orderInfo, final boolean result) {
        return Payment.builder()
                      // .coupon() TODO - 이거 생각 못했네...
                      .order(Order.from(orderInfo))
                      .paymentStatus(result ? SUCCESS : FAIL)
                      .paymentMethod(CASH)
                      .amount(orderInfo.getFinalPrice())
                      .build();
    }

    public PaymentInfo toInfo() {
        return PaymentInfo.builder()
                          .id(id)
                          .orderInfo(order.toInfo())
                          .paymentStatus(paymentStatus)
                          .paymentMethod(paymentMethod)
                          .amount(amount)
                          .build();
    }
}
