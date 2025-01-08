package kr.hhplus.be.server.api.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.api.member.request.PointChargeRequest;
import kr.hhplus.be.server.api.member.request.PointUseRequest;
import kr.hhplus.be.server.api.member.response.MemberResponse;
import kr.hhplus.be.server.api.member.response.PointHistoryResponse;
import kr.hhplus.be.server.api.member.response.PointResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "포인트 API", description = "유저 포인트 관련")
@RequestMapping("/api/v1/member/point")
public interface MemberApi {

    @Operation(
            summary = "포인트 조회",
            description = "현재 유저가 보유한 포인트를 조회 합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "포인트 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PointResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponse> findMemberById(@PathVariable("memberId") Long memberId);

    @Operation(
            summary = "포인트 충전",
            description = "유저의 포인트를 요청 받은 만큼 충전 합니다.",
            responses = {
                   @ApiResponse(
                           responseCode = "200",
                           description = "포인트 충전 성공",
                           content = @Content(
                                   mediaType = "application/json",
                                   schema = @Schema(implementation = PointResponse.class)
                           )
                   )
            }
    )
    @PostMapping("/{memberId}/charge")
    public ResponseEntity<PointHistoryResponse> chargePointById(@PathVariable("memberId") Long memberId, PointChargeRequest pointChargeRequest);

    @Operation(
            summary = "포인트 사용",
            description = "유저의 포인트를 요청 받은 만큼 사용 합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "포인트 사용 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PointResponse.class)
                            )
                    )
            }
    )
    @PostMapping("/{memberId}/use")
    public ResponseEntity<PointHistoryResponse> userPointById(@PathVariable("memberId") Long memberId, PointUseRequest pointUseRequest);
}
