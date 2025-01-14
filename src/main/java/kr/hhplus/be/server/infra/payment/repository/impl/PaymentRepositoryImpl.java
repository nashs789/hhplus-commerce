package kr.hhplus.be.server.infra.payment.repository.impl;

import jakarta.persistence.EntityManager;
import kr.hhplus.be.server.domain.order.info.OrderInfo;
import kr.hhplus.be.server.domain.payment.info.PaymentInfo;
import kr.hhplus.be.server.domain.payment.repository.PaymentRepository;
import kr.hhplus.be.server.infra.order.entity.Order;
import kr.hhplus.be.server.infra.payment.entity.Payment;
import kr.hhplus.be.server.infra.payment.repository.PaymentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static kr.hhplus.be.server.infra.payment.entity.Payment.PaymentMethod.CASH;
import static kr.hhplus.be.server.infra.payment.entity.Payment.PaymentStatus.FAIL;
import static kr.hhplus.be.server.infra.payment.entity.Payment.PaymentStatus.SUCCESS;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final EntityManager entityManager;
    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public PaymentInfo savePaymentResult(final boolean result, final OrderInfo orderInfo) {
        Order order = Order.builder()
                           .id(orderInfo.getId())
                           .build();
        Payment payment = Payment.builder()
                                 .order(order)
                                 .paymentStatus(result ? SUCCESS : FAIL)
                                 .paymentMethod(CASH)
                                 .amount(orderInfo.getFinalPrice())
                                 .build();
        entityManager.persist(payment);
        entityManager.flush();
        entityManager.clear();

        return entityManager.find(Payment.class, payment.getId())
                            .toInfo();
    }
}
