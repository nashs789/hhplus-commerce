package kr.hhplus.be.server.api.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.api.order.response.OrderDetailResponse;
import kr.hhplus.be.server.api.order.response.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "주문 API", description = "주문 관련")
@RequestMapping("/api/v1/order")
public interface OrderApi {

    @Operation(
            summary = "주문",
            description = "주문",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "주문 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderDetailResponse.class)
                            )
                    )
            }
    )
    @PostMapping("/{memberId}")
    ResponseEntity<OrderResponse> createOrder(@PathVariable Long memberId);
}
