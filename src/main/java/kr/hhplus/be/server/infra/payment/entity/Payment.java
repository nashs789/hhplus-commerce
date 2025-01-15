package kr.hhplus.be.server.infra.payment.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.payment.info.PaymentInfo;
import kr.hhplus.be.server.infra.common.entity.Timestamp;
import kr.hhplus.be.server.infra.order.entity.Order;
import lombok.*;
import lombok.experimental.SuperBuilder;

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

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column
    private Long amount;

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
