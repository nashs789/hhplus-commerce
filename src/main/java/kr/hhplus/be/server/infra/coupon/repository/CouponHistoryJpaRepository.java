package kr.hhplus.be.server.infra.coupon.repository;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.infra.coupon.entity.CouponHistory;
import kr.hhplus.be.server.infra.coupon.entity.CouponHistory.CouponStatus;
import kr.hhplus.be.server.infra.coupon.entity.key.CouponHistoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CouponHistoryJpaRepository extends JpaRepository<CouponHistory, CouponHistoryId> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT ch
          FROM Coupon c
          JOIN CouponHistory ch
            ON c.id = ch.id.couponId
         WHERE ch.id.memberId = :memberId
           AND c.id = :couponId
    """)
    Optional<CouponHistory> findCouponHistoryByIdWithLock(
            @Param("couponId") final Long couponId,
            @Param("memberId") final Long memberId
    );

    @Modifying
    @Query("""
        UPDATE CouponHistory ch
           SET ch.status = :couponStatus
         WHERE ch.id.memberId = :memberId
           AND ch.id.couponId = :couponId
    """)
    void changeCouponHistoryStatus(
            @Param("couponId") final Long couponId,
            @Param("couponStatus") final CouponStatus couponStatus,
            @Param("memberId") final Long memberId
    );

    @Query("""
        SELECT ch
          FROM CouponHistory ch
         WHERE ch.id.memberId = :memberId
    """)
    Optional<List<CouponHistory>> findCouponHistoryMemberById(@Param("memberId") final Long memberId);

    @Query("""
        SELECT ch
          FROM CouponHistory ch
         WHERE ch.id.couponId = :couponId
    """)
    Optional<List<CouponHistory>> findCouponHistoryCouponId(@Param("couponId") final Long couponId);

    @Query("""
        SELECT ch
          FROM Coupon c
          JOIN CouponHistory ch
            ON c.id = ch.id.couponId
         WHERE c.id = :couponId
           AND ch.id.memberId = :memberId
    """)
    Optional<CouponHistory> isDuplicated(@Param("couponId") final Long couponId, final Long memberId);
}
