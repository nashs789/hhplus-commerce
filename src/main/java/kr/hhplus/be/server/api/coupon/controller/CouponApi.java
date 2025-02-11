package kr.hhplus.be.server.api.coupon.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.api.coupon.response.CouponHistoryResponse;
import kr.hhplus.be.server.api.coupon.response.CouponResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "쿠폰 API", description = "쿠폰 관련")
@RequestMapping("/api/v1/coupon")
public interface CouponApi {

    @Operation(
            summary = "보유 쿠폰 조회",
            description = "보유한 쿠폰을 조회 합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "보유 쿠폰 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CouponResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/member/{memberId}/list")
    ResponseEntity<List<CouponHistoryResponse>> findCouponHistoryById(@PathVariable("memberId") Long memberId);

    @Operation(
            summary = "쿠폰 발급 신청",
            description = "쿠폰 발급 신청 합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "보유 발급 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CouponResponse.class)
                            )
                    )
            }
    )
    @PostMapping("/{couponId}/member/{memberId}")
    ResponseEntity<Boolean> applyCouponById(
            @PathVariable("couponId") Long couponId,
            @PathVariable("memberId") Long memberId
    );
}
