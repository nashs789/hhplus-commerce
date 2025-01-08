package kr.hhplus.be.server.api.member.controller;

import kr.hhplus.be.server.api.product.response.ProductResponse;
import kr.hhplus.be.server.api.member.request.CartRequest;
import kr.hhplus.be.server.api.member.response.CartResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CartController implements CartApi {

    @Override
    public ResponseEntity<CartResponse> findCartById(Long userId) {
        return ResponseEntity.ok(
                new CartResponse(
                        List.of(

                        )
                )
        );
    }

    @Override
    public ResponseEntity<CartResponse> addCartById(Long userId, List<CartRequest> cartRequests) {
        return ResponseEntity.ok(
                new CartResponse(
                        List.of(

                        )
                )
        );
    }

    @Override
    public ResponseEntity<CartResponse> deleteCartById(Long userId, List<CartRequest> cartRequests) {
        return ResponseEntity.ok(
                new CartResponse(
                        List.of(

                        )
                )
        );
    }
}
