package kr.hhplus.be.server.api.coupon.response;

import kr.hhplus.be.server.domain.coupon.info.CouponInfo;
import kr.hhplus.be.server.domain.member.info.MemberInfo;
import kr.hhplus.be.server.infra.coupon.entity.CouponHistory.CouponStatus;

public record CouponHistoryResponse(
        CouponInfo couponInfo,
        MemberInfo memberInfo,
        CouponStatus status
) {
}
