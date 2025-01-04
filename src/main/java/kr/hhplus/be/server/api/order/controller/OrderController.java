package kr.hhplus.be.server.api.order.controller;

import kr.hhplus.be.server.api.order.request.OrderRequest;
import kr.hhplus.be.server.api.order.response.OrderResponse;
import kr.hhplus.be.server.api.product.response.ProductResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class OrderController implements OrderApi {

    @Override
    public ResponseEntity<List<OrderResponse>> createOrder(OrderRequest orderRequest) {
        return ResponseEntity.ok(
                List.of(
                        new OrderResponse(1L, new ProductResponse(1L, "연필", 1_000L, 100L)),
                        new OrderResponse(1L, new ProductResponse(2L, "필통", 3_000L, 50L)),
                        new OrderResponse(1L, new ProductResponse(3L, "볼펜", 1_500L, 1000L))
                )
        );
    }
}
