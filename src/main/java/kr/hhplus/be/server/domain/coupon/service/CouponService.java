package kr.hhplus.be.server.domain.coupon.service;

import kr.hhplus.be.server.domain.coupon.exception.CouponException;
import kr.hhplus.be.server.domain.coupon.info.CouponHistoryInfo;
import kr.hhplus.be.server.domain.coupon.info.CouponInfo;
import kr.hhplus.be.server.domain.coupon.repository.CouponRepository;
import kr.hhplus.be.server.domain.member.info.MemberInfo;
import kr.hhplus.be.server.global.aop.DistributedLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kr.hhplus.be.server.domain.coupon.exception.CouponException.CouponExceptionCode.PUBLISH_DUPLICATED_COUPON;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    @Transactional(readOnly = true)
    public CouponInfo findCouponById(final Long couponId) {
        return couponRepository.findCouponById(couponId);
    }

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

    @DistributedLock(key = "#couponId")
    public CouponHistoryInfo applyPublishedCoupon(final Long couponId, final MemberInfo memberInfo) {
        CouponInfo couponInfo = couponRepository.findCouponById(couponId);

        couponInfo.checkAvailability();

        if(couponRepository.isDuplicated(couponInfo.getId(), memberInfo.getId())) {
            throw new CouponException(PUBLISH_DUPLICATED_COUPON);
        }

        return couponRepository.applyPublishedCoupon(couponInfo, memberInfo.getId());
    }
}
