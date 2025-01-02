package kr.hhplus.be.server.api.payment.controller;

import kr.hhplus.be.server.api.payment.request.PaymentRequest;
import kr.hhplus.be.server.api.payment.response.PaymentResponse;
import org.springframework.http.ResponseEntity;

public class PaymentController implements PaymentApi {

    @Override
    public ResponseEntity<PaymentResponse> paymentProgress(PaymentRequest paymentRequest) {
        return ResponseEntity.ok(new PaymentResponse(1L));
    }
}
