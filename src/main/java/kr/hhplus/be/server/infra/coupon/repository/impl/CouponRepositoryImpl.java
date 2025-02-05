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

    private final CouponJpaRepository couponJpaRepository;
    private final CouponHistoryJpaRepository couponHistoryJpaRepository;

    @Override
    public List<CouponInfo> findAll() {
        return couponJpaRepository.findAll()
                                  .stream()
                                  .map(Coupon::toInfo)
                                  .toList();
    }

    @Override
    public CouponInfo findCouponById(final Long couponId) {
        return couponJpaRepository.findById(couponId)
                                  .orElseThrow(() -> new CouponException(NOT_EXIST_COUPON))
                                  .toInfo();
    }

    @Override
    public CouponHistoryInfo findCouponHistoryByIdWithLock(final Long couponId, final Long memberId) {
        return couponHistoryJpaRepository.findCouponHistoryByIdWithLock(couponId, memberId)
                                         .orElseThrow(() -> new CouponException(NOT_EXIST_COUPON_HISTORY))
                                         .toInfo();
    }

    @Override
    public void changeCouponHistoryStatus(final CouponHistoryInfo couponHistoryInfo, final Long memberId) {
        couponHistoryJpaRepository.changeCouponHistoryStatus(couponHistoryInfo.getId().getCouponId(), couponHistoryInfo.getStatus(), memberId);
    }

    @Override
    public List<CouponHistoryInfo> findCouponHistoryMemberById(final Long memberId) {
        return couponHistoryJpaRepository.findCouponHistoryMemberById(memberId)
                                         .orElseThrow(() -> new CouponException(NOT_EXIST_COUPON_HISTORY))
                                         .stream()
                                         .map(CouponHistory::toInfo)
                                         .toList();
    }

    @Override
    public boolean isDuplicated(Long couponId, Long memberId) {
        return couponHistoryJpaRepository.isDuplicated(couponId, memberId)
                                         .isPresent();
    }

    @Override
    public CouponHistoryInfo applyPublishedCoupon(final CouponInfo couponInfo, final Long memberId) {
        Coupon coupon = Coupon.from(couponInfo);
        CouponHistory couponHistory = CouponHistory.builder()
                                                   .id(new CouponHistoryId(couponInfo.getId(), memberId))
                                                   .coupon(coupon)
                                                   .member(Member.builder()
                                                                 .id(memberId)
                                                                 .build())
                                                   .status(NOT_USED)
                                                   .build();

        couponJpaRepository.save(coupon);

        return couponHistoryJpaRepository.save(couponHistory)
                                         .toInfo();
    }
}
