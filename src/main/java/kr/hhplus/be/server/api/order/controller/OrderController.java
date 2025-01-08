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

                )
        );
    }
}
