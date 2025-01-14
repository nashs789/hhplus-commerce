package kr.hhplus.be.server.domain.payment.service;

import kr.hhplus.be.server.domain.order.info.OrderInfo;
import kr.hhplus.be.server.domain.payment.info.PaymentInfo;
import kr.hhplus.be.server.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentInfo savePaymentResult(final boolean result, final OrderInfo orderInfo) {
        return paymentRepository.savePaymentResult(result, orderInfo);
    }
}
