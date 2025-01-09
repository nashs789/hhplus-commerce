package kr.hhplus.be.server.infra.coupon.repository.impl;

import kr.hhplus.be.server.domain.coupon.exception.CouponException;
import kr.hhplus.be.server.domain.coupon.info.CouponHistoryInfo;
import kr.hhplus.be.server.domain.coupon.info.CouponInfo;
import kr.hhplus.be.server.domain.coupon.repository.CouponRepository;
import kr.hhplus.be.server.infra.coupon.entity.Coupon;
import kr.hhplus.be.server.infra.coupon.entity.CouponHistory;
import kr.hhplus.be.server.infra.coupon.entity.key.CouponHistoryId;
import kr.hhplus.be.server.infra.coupon.repository.CouponHistoryJpaRepository;
import kr.hhplus.be.server.infra.coupon.repository.CouponJpaRepository;
import kr.hhplus.be.server.infra.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.hhplus.be.server.domain.coupon.exception.CouponException.CouponExceptionCode.NOT_EXIST_COUPON;
import static kr.hhplus.be.server.domain.coupon.exception.CouponException.CouponExceptionCode.NOT_EXIST_COUPON_HISTORY;
import static kr.hhplus.be.server.infra.coupon.entity.CouponHistory.CouponStatus.NOT_USED;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {

    /** 쿠폰 정보 */
    private final CouponJpaRepository couponJpaRepository;
    /** 쿠폰 발급 이력 */
    private final CouponHistoryJpaRepository couponHistoryJpaRepository;

    @Override
    public CouponInfo findCouponById(final Long couponId) {
        return couponJpaRepository.findById(couponId)
                                  .orElseThrow(() -> new CouponException(NOT_EXIST_COUPON))
                                  .toInfo();
    }

    @Override
    public CouponInfo findCouponByIdWithLock(final Long couponId) {
        return couponJpaRepository.findCouponByIdWithLock(couponId)
                                  .orElseThrow(() -> new CouponException(NOT_EXIST_COUPON))
                                  .toInfo();
    }

    @Override
    public List<CouponHistoryInfo> findCouponHistoryById(final Long memberId) {
        return couponHistoryJpaRepository.findCouponHistoryById(memberId)
                                         .orElseThrow(() -> new CouponException(NOT_EXIST_COUPON_HISTORY))
                                         .stream()
                                         .map(CouponHistory::toInfo)
                                         .toList();
    }

    @Override
    public CouponHistoryInfo applyPublishedCoupon(final Long couponId, final Long memberId) {
        CouponHistory couponHistory = CouponHistory.builder()
                                                   .id(new CouponHistoryId(couponId, memberId))
                                                   .coupon(Coupon.builder()
                                                                 .id(couponId)
                                                                 .build())
                                                   .member(Member.builder()
                                                                 .id(memberId)
                                                                 .build())
                                                   .status(NOT_USED)
                                                   .build();

        return couponHistoryJpaRepository.save(couponHistory)
                                         .toInfo();
    }
}
