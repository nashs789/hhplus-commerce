package kr.hhplus.be.server.api.coupon.controller;

import kr.hhplus.be.server.api.coupon.response.CouponResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CouponController implements CouponApi{

    @Override
    public ResponseEntity<List<CouponResponse>> findCouponById(Long userId) {
        return ResponseEntity.ok(
                List.of(
                        new CouponResponse(1L, "couponA"),
                        new CouponResponse(1L, "couponB"),
                        new CouponResponse(1L, "couponC")
                )
        );
    }

    @Override
    public ResponseEntity<CouponResponse> applyCouponById(Long userId) {
        return ResponseEntity.ok(new CouponResponse(1L, "couponA"));
    }
}
