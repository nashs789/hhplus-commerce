package kr.hhplus.be.server.domain.coupon.service;

import kr.hhplus.be.server.domain.coupon.info.CouponHistoryInfo;
import kr.hhplus.be.server.domain.coupon.info.CouponInfo;
import kr.hhplus.be.server.domain.coupon.repository.CouponRepository;
import kr.hhplus.be.server.domain.member.info.MemberInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponCacheService couponCacheService;
    private final CouponApplyCacheService couponApplyCacheService;
    private final CouponRepository couponRepository;

    @Transactional
    public CouponHistoryInfo findCouponHistoryByIdWithLock(final Long couponId, final Long memberId) {
        return couponRepository.findCouponHistoryByIdWithLock(couponId, memberId);
    }

    @Transactional
    public void changeCouponHistoryStatus(final CouponHistoryInfo couponHistoryInfo, final Long memberId) {
        couponRepository.changeCouponHistoryStatus(couponHistoryInfo, memberId);
    }

    @Transactional(readOnly = true)
    public List<CouponHistoryInfo> findCouponHistoryMemberById(final Long memberId) {
        return couponRepository.findCouponHistoryMemberById(memberId);
    }

    @Transactional
    public boolean applyPublishedCoupon(final Long couponId, final MemberInfo memberInfo) {
        if(couponCacheService.isAppliable(couponId)) {
            couponApplyCacheService.applyCoupon(couponId, memberInfo.getId());

            return true;
        }

        CouponInfo couponInfo = couponRepository.findCouponById(couponId);

        couponCacheService.addCouponCache(couponId, couponInfo.getRestCouponCount());
        couponApplyCacheService.applyCoupon(couponId, memberInfo.getId());

        return couponInfo.checkAvailability();
    }
}
