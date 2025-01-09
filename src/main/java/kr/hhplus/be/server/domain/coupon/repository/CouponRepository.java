package kr.hhplus.be.server.domain.coupon.repository;

import kr.hhplus.be.server.domain.coupon.info.CouponHistoryInfo;
import kr.hhplus.be.server.domain.coupon.info.CouponInfo;
import kr.hhplus.be.server.domain.member.info.MemberInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRepository {
    CouponInfo findCouponById(Long couponId);
    CouponInfo findCouponByIdWithLock(Long couponId);
    List<CouponHistoryInfo> findCouponHistoryById(Long memberId);
    CouponHistoryInfo applyPublishedCoupon(Long couponId, Long memberId);
}
