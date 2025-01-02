package kr.hhplus.be.server.api.user.controller;

import kr.hhplus.be.server.api.user.request.PointRequest;
import kr.hhplus.be.server.api.user.response.PointResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PointController implements PointApi{

    @Override
    public ResponseEntity<PointResponse> findPointById(@PathVariable("userId") final Long userId) {
        return ResponseEntity.ok(new PointResponse(1L, 10_000L));
    }

    @Override
    public ResponseEntity<PointResponse> chargePointById(
            @PathVariable("userId") final Long userId, final PointRequest pointRequest
    ) {
        return ResponseEntity.ok(new PointResponse(1L, 10_000L));
    }

    @Override
    public ResponseEntity<PointResponse> userPointById(
            @PathVariable("userId") final Long userId, final PointRequest pointRequest
    ) {
        return ResponseEntity.ok(new PointResponse(1L, 10_000L));
    }
}
