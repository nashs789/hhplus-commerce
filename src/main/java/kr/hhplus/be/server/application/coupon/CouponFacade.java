package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.info.CouponHistoryInfo;
import kr.hhplus.be.server.domain.coupon.info.CouponInfo;
import kr.hhplus.be.server.domain.coupon.service.CouponService;
import kr.hhplus.be.server.domain.member.info.MemberInfo;
import kr.hhplus.be.server.domain.member.service.MemberService;
import kr.hhplus.be.server.global.aop.DistributedLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CouponFacade {

    private final MemberService memberService;
    private final CouponService couponService;

    @DistributedLock(key = "#couponId")
    public CouponHistoryInfo applyCouponById(final Long couponId, final Long memberId) {
        MemberInfo memberInfo = memberService.findMemberById(memberId);

        return couponService.applyPublishedCoupon(couponId, memberInfo);
    }
}
