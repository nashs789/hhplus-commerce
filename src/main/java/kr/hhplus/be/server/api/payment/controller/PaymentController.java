package kr.hhplus.be.server.api.payment.controller;

import kr.hhplus.be.server.api.payment.request.PaymentRequest;
import kr.hhplus.be.server.api.payment.response.PaymentResponse;
import kr.hhplus.be.server.application.payment.facade.PaymentFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentController implements PaymentApi {

    private final PaymentFacade paymentFacade;

    @Override
    public ResponseEntity<PaymentResponse> paymentProgress(
            @PathVariable("memberId") final Long memberId,
            @PathVariable("orderId") final Long orderId,
            @RequestBody PaymentRequest paymentRequest
    ) {
        return ResponseEntity.ok(paymentFacade.paymentProgress(memberId, orderId, paymentRequest.couponId())
                                              .toResponse());
    }
}
