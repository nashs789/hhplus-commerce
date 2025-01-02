package kr.hhplus.be.server.api.user.controller;

import kr.hhplus.be.server.api.product.response.ProductResponse;
import kr.hhplus.be.server.api.user.request.CartRequest;
import kr.hhplus.be.server.api.user.response.CartResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class CartController implements CartApi {

    @Override
    public ResponseEntity<CartResponse> findCartById(Long userId) {
        return ResponseEntity.ok(
                new CartResponse(
                        List.of(
                                new ProductResponse(10L, "지갑", 10_000L, 45L),
                                new ProductResponse(11L, "키링", 1200L, 1009L),
                                new ProductResponse(12L, "인형", 12_000L, 40L)
                        )
                )
        );
    }

    @Override
    public ResponseEntity<CartResponse> addCartById(Long userId, List<CartRequest> cartRequests) {
        return ResponseEntity.ok(
                new CartResponse(
                        List.of(
                                new ProductResponse(10L, "지갑", 10_000L, 45L),
                                new ProductResponse(11L, "키링", 1200L, 1009L),
                                new ProductResponse(12L, "인형", 12_000L, 40L)
                        )
                )
        );
    }

    @Override
    public ResponseEntity<CartResponse> deleteCartById(Long userId, List<CartRequest> cartRequests) {
        return ResponseEntity.ok(
                new CartResponse(
                        List.of(
                                new ProductResponse(10L, "지갑", 10_000L, 45L),
                                new ProductResponse(11L, "키링", 1200L, 1009L),
                                new ProductResponse(12L, "인형", 12_000L, 40L)
                        )
                )
        );
    }
}
