package kr.hhplus.be.server.domain.payment.service;

import kr.hhplus.be.server.domain.order.info.OrderInfo;
import kr.hhplus.be.server.domain.payment.info.PaymentInfo;
import kr.hhplus.be.server.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static kr.hhplus.be.server.infra.payment.entity.Payment.PaymentStatus.SUCCESS;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentInfo findPaymentById(final Long paymentId) {
        return paymentRepository.findPaymentById(paymentId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PaymentInfo savePaymentResult(final boolean result, final OrderInfo orderInfo) {
        return paymentRepository.savePaymentResult(result, orderInfo);
    }

    @Transactional(readOnly = true)
    public List<PaymentInfo> findCurrentThreeDaysPayment() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threeDaysBefore = now.minusDays(3);

        return paymentRepository.findCurrentThreeDaysPayment(threeDaysBefore, now, SUCCESS);
    }
}
