package kr.hhplus.be.server.api.coupon.controller;

import kr.hhplus.be.server.api.coupon.response.CouponHistoryResponse;
import kr.hhplus.be.server.api.coupon.response.CouponResponse;
import kr.hhplus.be.server.application.coupon.CouponFacade;
import kr.hhplus.be.server.domain.coupon.info.CouponHistoryInfo;
import kr.hhplus.be.server.domain.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CouponController implements CouponApi{

    private final CouponService couponService;
    private final CouponFacade couponFacade;

    @Override
    public ResponseEntity<CouponResponse> findCouponById(
            @PathVariable("couponId") final Long couponId
    ) {
        return ResponseEntity.ok(couponService.findCouponById(couponId)
                                              .toResponse()
        );
    }

    @Override
    public ResponseEntity<List<CouponHistoryResponse>> findCouponHistoryById(
            @PathVariable("memberId") final Long memberId
    ) {
        return ResponseEntity.ok(couponService.findCouponHistoryById(memberId)
                                              .stream()
                                              .map(CouponHistoryInfo::toResponse)
                                              .toList());
    }

    @Override
    public ResponseEntity<CouponHistoryResponse> applyCouponById(
            @PathVariable("couponId") final Long couponId,
            @PathVariable("memberId") final Long memberId
    ) {
        return ResponseEntity.ok(couponFacade.applyCouponById(couponId, memberId)
                                             .toResponse());
    }
}
