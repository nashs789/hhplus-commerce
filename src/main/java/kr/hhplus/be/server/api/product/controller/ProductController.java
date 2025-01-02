package kr.hhplus.be.server.api.product.controller;

import kr.hhplus.be.server.api.product.response.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController implements ProductApi {

    @Override
    public ResponseEntity<ProductResponse> findProductById(Long productId) {
        return ResponseEntity.ok(new ProductResponse(1L, "컴퓨터", 10_100_000L, 10L));
    }

    @Override
    public ResponseEntity<List<ProductResponse>> findProductRanking() {
        return ResponseEntity.ok(
                List.of(
                        new ProductResponse(1L, "컴퓨터", 10_100_000L, 10L),
                        new ProductResponse(2L, "키보드", 100_000L, 50L),
                        new ProductResponse(3L, "마우스", 50_000L, 100L)
                )
        );
    }
}
