package kr.hhplus.be.server.api.payment.response;

import kr.hhplus.be.server.api.order.response.OrderResponse;
import kr.hhplus.be.server.infra.payment.entity.Payment.PaymentMethod;
import kr.hhplus.be.server.infra.payment.entity.Payment.PaymentStatus;

public record PaymentResponse(
        Long id,
        OrderResponse orderResponse,
        PaymentStatus paymentStatus,
        PaymentMethod paymentMethod,
        Long amount
) {
}
