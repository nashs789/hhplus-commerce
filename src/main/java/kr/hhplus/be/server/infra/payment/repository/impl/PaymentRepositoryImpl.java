package kr.hhplus.be.server.infra.payment.repository.impl;

import kr.hhplus.be.server.domain.order.info.OrderInfo;
import kr.hhplus.be.server.domain.payment.info.PaymentInfo;
import kr.hhplus.be.server.domain.payment.repository.PaymentRepository;
import kr.hhplus.be.server.infra.payment.entity.Payment;
import kr.hhplus.be.server.infra.payment.repository.PaymentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public PaymentInfo findPaymentById(final Long paymentId) {
        return paymentJpaRepository.findOneById(paymentId)
                                   .toInfo();
    }

    @Override
    public PaymentInfo savePaymentResult(final boolean result, final OrderInfo orderInfo) {
        Payment payment = Payment.of(orderInfo, result);

        return paymentJpaRepository.save(payment)
                                   .toInfo();
    }
}
