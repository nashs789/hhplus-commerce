package kr.hhplus.be.server.domain.payment.info;

import kr.hhplus.be.server.api.payment.response.PaymentResponse;
import kr.hhplus.be.server.domain.order.info.OrderInfo;
import kr.hhplus.be.server.infra.payment.entity.Payment.PaymentMethod;
import kr.hhplus.be.server.infra.payment.entity.Payment.PaymentStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentInfo {
    private Long id;
    private OrderInfo orderInfo;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private Long amount;

    public PaymentResponse toResponse() {
        return new PaymentResponse(
          id, orderInfo.toResponse(), paymentStatus, paymentMethod, amount
        );
    }
}
