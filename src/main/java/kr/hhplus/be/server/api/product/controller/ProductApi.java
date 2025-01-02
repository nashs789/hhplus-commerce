package kr.hhplus.be.server.api.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.api.product.response.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "상품 API", description = "상품 관련")
@RequestMapping("/api/v1/product")
public interface ProductApi {

    @Operation(
            summary = "상품 조회",
            description = "상품을 조회 합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "상품 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProductResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> findProductById(@PathVariable("productId") Long productId);

    @Operation(
            summary = "인기 상품 조회",
            description = "인기 상품을 조회 합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "인기 상품 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProductResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/ranking")
    public ResponseEntity<List<ProductResponse>> findProductRanking();
}