package kr.hhplus.be.server.domain.payment.repository;

import kr.hhplus.be.server.domain.order.info.OrderInfo;
import kr.hhplus.be.server.domain.payment.info.PaymentInfo;
import kr.hhplus.be.server.infra.payment.entity.Payment;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepository {

    PaymentInfo findPaymentById(Long paymentId);
    PaymentInfo savePaymentResult(boolean result, OrderInfo orderInfo);
    List<PaymentInfo> findCurrentThreeDaysPayment(LocalDateTime threeDaysBefore, LocalDateTime now, Payment.PaymentStatus paymentStatus);
}
