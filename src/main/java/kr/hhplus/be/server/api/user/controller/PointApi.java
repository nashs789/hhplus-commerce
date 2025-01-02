package kr.hhplus.be.server.api.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.api.user.request.PointRequest;
import kr.hhplus.be.server.api.user.response.PointResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "포인트 API", description = "유저 포인트 관련")
@RequestMapping("/api/v1/user/point")
public interface PointApi {

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
    @GetMapping("/{userId}")
    public ResponseEntity<PointResponse> findPointById(@PathVariable("userId") Long userId);

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
    @PostMapping("/{userId}/charge")
    public ResponseEntity<PointResponse> chargePointById(@PathVariable("userId") Long userId, PointRequest pointRequest);

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
    @PostMapping("/{userId}/use")
    public ResponseEntity<PointResponse> userPointById(@PathVariable("userId") Long userId, PointRequest pointRequest);
}
