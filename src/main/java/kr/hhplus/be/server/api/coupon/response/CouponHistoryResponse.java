package kr.hhplus.be.server.api.coupon.response;

import kr.hhplus.be.server.infra.coupon.entity.CouponHistory.CouponStatus;

public record CouponHistoryResponse(
        Long couponId,
        Long memberId,
        CouponStatus status
) {
}
