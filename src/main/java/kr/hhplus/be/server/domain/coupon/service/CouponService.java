package kr.hhplus.be.server.domain.coupon.service;

import kr.hhplus.be.server.domain.coupon.exception.CouponException;
import kr.hhplus.be.server.domain.coupon.info.CouponHistoryInfo;
import kr.hhplus.be.server.domain.coupon.info.CouponInfo;
import kr.hhplus.be.server.domain.coupon.repository.CouponRepository;
import kr.hhplus.be.server.domain.member.info.MemberInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    @Transactional(readOnly = true)
    public CouponInfo findCouponById(final Long couponId) {
        return couponRepository.findCouponById(couponId);
    }

    @Transactional
    public CouponInfo findCouponByIdWithLock(final Long couponId) {
        return couponRepository.findCouponByIdWithLock(couponId);
    }

    @Transactional(readOnly = true)
    public List<CouponHistoryInfo> findCouponHistoryById(final Long memberId) {
        return couponRepository.findCouponHistoryById(memberId);
    }

    @Transactional
    public CouponHistoryInfo applyPublishedCoupon(final CouponInfo couponInfo, final MemberInfo memberInfo) {
        couponInfo.checkAvailability();

        return couponRepository.applyPublishedCoupon(couponInfo.getId(), memberInfo.getId());
    }
}
