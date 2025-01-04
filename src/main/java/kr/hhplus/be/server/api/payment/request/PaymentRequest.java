package kr.hhplus.be.server.api.payment.request;

public record PaymentRequest(
        Long userId,
        Long orderId
) {
}
