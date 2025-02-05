package kr.hhplus.be.server.domain.coupon.repository;

import kr.hhplus.be.server.domain.coupon.info.CouponHistoryInfo;
import kr.hhplus.be.server.domain.coupon.info.CouponInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRepository {
    List<CouponInfo> findAll();
    CouponInfo findCouponById(Long couponId);
    CouponHistoryInfo findCouponHistoryByIdWithLock(Long couponId, Long memberId);
    void changeCouponHistoryStatus(CouponHistoryInfo couponHistoryInfo, Long memberId);
    List<CouponHistoryInfo> findCouponHistoryMemberById(Long memberId);
    boolean isDuplicated(Long couponId, Long memberId);
    CouponHistoryInfo applyPublishedCoupon(Long couponId, Long memberId);
}
