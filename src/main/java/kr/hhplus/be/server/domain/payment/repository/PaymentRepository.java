package kr.hhplus.be.server.domain.payment.repository;

import kr.hhplus.be.server.domain.order.info.OrderInfo;
import kr.hhplus.be.server.domain.payment.info.PaymentInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository {

    PaymentInfo savePaymentResult(boolean result, OrderInfo orderInfo);
}
