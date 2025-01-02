package kr.hhplus.be.server.api.payment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.api.payment.request.PaymentRequest;
import kr.hhplus.be.server.api.payment.response.PaymentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "주문 API", description = "주문 관련")
@RequestMapping("/api/v1/payment")
public interface PaymentApi {

    @Operation(
            summary = "결제",
            description = "결제",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "결제 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PaymentResponse.class)
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<PaymentResponse> paymentProgress(PaymentRequest paymentRequest);
}
