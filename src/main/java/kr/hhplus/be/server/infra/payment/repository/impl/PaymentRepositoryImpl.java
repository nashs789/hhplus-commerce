package kr.hhplus.be.server.infra.payment.repository.impl;

import kr.hhplus.be.server.domain.order.info.OrderInfo;
import kr.hhplus.be.server.domain.payment.info.PaymentInfo;
import kr.hhplus.be.server.domain.payment.repository.PaymentRepository;
import kr.hhplus.be.server.infra.payment.entity.Payment;
import kr.hhplus.be.server.infra.payment.entity.Payment.PaymentStatus;
import kr.hhplus.be.server.infra.payment.repository.PaymentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

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
        Payment payment = Payment.from(orderInfo, result);

        return paymentJpaRepository.save(payment)
                                   .toInfo();
    }

    @Override
    public List<PaymentInfo> findCurrentThreeDaysPayment(LocalDateTime threeDaysBefore, LocalDateTime now, PaymentStatus paymentStatus) {
        return paymentJpaRepository.findCurrentThreeDaysPayment(threeDaysBefore, now, paymentStatus)
                                   .stream()
                                   .map(Payment::toInfo)
                                   .toList();
    }
}
