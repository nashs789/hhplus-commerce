package kr.hhplus.be.server.infra.coupon.repository;

import kr.hhplus.be.server.infra.coupon.entity.CouponHistory;
import kr.hhplus.be.server.infra.coupon.entity.key.CouponHistoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CouponHistoryJpaRepository extends JpaRepository<CouponHistory, CouponHistoryId> {

    @Query("""
        SELECT ch
          FROM Coupon c
          JOIN CouponHistory ch
            ON c.id = ch.coupon.id
    """)
    Optional<List<CouponHistory>> findCouponHistoryMemberById(@Param("memberId") final Long memberId);

    @Query("""
        SELECT ch
          FROM Coupon c
          JOIN CouponHistory ch
            ON c.id = ch.coupon.id
         WHERE c.id = :couponId
           AND ch.member.id = :memberId
    """)
    Optional<CouponHistory> isDuplicated(@Param("couponId") final Long couponId, final Long memberId);
}
