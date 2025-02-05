package kr.hhplus.be.server.domain.coupon.service;

import kr.hhplus.be.server.domain.coupon.cache.CouponApplyCache;
import kr.hhplus.be.server.domain.coupon.cache.CouponCache;
import kr.hhplus.be.server.domain.coupon.cache.CouponHistoryCache;
import kr.hhplus.be.server.domain.coupon.exception.CouponException;
import kr.hhplus.be.server.domain.coupon.info.CouponHistoryInfo;
import kr.hhplus.be.server.domain.coupon.info.CouponInfo;
import kr.hhplus.be.server.domain.coupon.info.CouponPublishEvent;
import kr.hhplus.be.server.domain.coupon.repository.CouponRepository;
import kr.hhplus.be.server.domain.member.info.MemberInfo;
import kr.hhplus.be.server.infra.coupon.entity.CouponHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static kr.hhplus.be.server.domain.coupon.exception.CouponException.CouponExceptionCode.NOT_SUFFICIENT_QUANTITY;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponCache couponCache;
    private final CouponApplyCache couponApplyCache;
    private final CouponHistoryCache couponHistoryCache;
    private final CouponRepository couponRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public CouponInfo findByCouponId(final Long couponId) {
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

    @Transactional
    public boolean applyPublishedCoupon(final Long couponId, final MemberInfo memberInfo) {
        couponHistoryCache.checkAppliedHistory(couponId, memberInfo.getId());

        if(couponCache.isAppliable(couponId)) {
            couponApplyCache.applyCoupon(couponId, memberInfo.getId());

            return true;
        }

        CouponInfo couponInfo = couponRepository.findCouponById(couponId);

        couponCache.addCouponCache(couponId, couponInfo.getRestCouponCount());
        couponApplyCache.applyCoupon(couponId, memberInfo.getId());

        return couponInfo.checkAvailability();
    }

    // TODO - 장애 났을 때 데이터 정합성 지못미... 일단 구현 먼저
    @Async
    @Transactional
    public void publishCoupon(final Long couponId) throws CouponException {
        if(!couponCache.isAppliable(couponId)) {
            throw new CouponException(NOT_SUFFICIENT_QUANTITY);
        }

        long left = couponCache.decreaseCouponCount(couponId);
        Long memberId = couponApplyCache.getNextApplicant(couponId);

        log.info("쿠폰: {}, 신청자: {}, 잔량: {}", couponId, memberId, left);

        if(Objects.nonNull(memberId)) {
            couponHistoryCache.addCouponHistory(couponId, memberId);
            couponRepository.applyPublishedCoupon(couponId, memberId);
        }

        eventPublisher.publishEvent(new CouponPublishEvent(couponId));
    }
}
