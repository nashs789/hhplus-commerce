package kr.hhplus.be.server.api.order.controller;

import kr.hhplus.be.server.api.order.response.OrderResponse;
import kr.hhplus.be.server.application.order.OrderFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController implements OrderApi {

    private final OrderFacade orderFacade;

    @Override
    public ResponseEntity<OrderResponse> createOrder(
            @PathVariable final Long memberId
    ) {
        return ResponseEntity.ok(orderFacade.createOrder(memberId)
                                           .toResponse());
    }
}
