package kr.hhplus.be.server.api.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.api.member.request.CartRequest;
import kr.hhplus.be.server.api.member.response.CartResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "장바구니 API", description = "장바구니 관련")
@RequestMapping("/api/v1/user/cart")
public interface CartApi {

    @Operation(
            summary = "장바구니 조회",
            description = "유저 장바구니 품목 조회",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "장바구니 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CartResponse.class)
                            )
                    )
            }
    )
    @PostMapping("/{userId}/add")
    public ResponseEntity<CartResponse> findCartById(@PathVariable("userId") Long userId);

    @Operation(
            summary = "장바구니 품목 추가",
            description = "유저 장바구니 품목 추가",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "장바구니 품목 추가 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CartResponse.class)
                            )
                    )
            }
    )
    @PostMapping("/{userId}/delete")
    public ResponseEntity<CartResponse> addCartById(@PathVariable("userId") Long userId, List<CartRequest> cartRequests);

    @Operation(
            summary = "장바구니 품목 삭제",
            description = "유저 장바구니 품목 삭제",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "장바구니 품목 삭제 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CartResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/{userId}")
    public ResponseEntity<CartResponse> deleteCartById(@PathVariable("userId") Long userId, List<CartRequest> cartRequests);
}
