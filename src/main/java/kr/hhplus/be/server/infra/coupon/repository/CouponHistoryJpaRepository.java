package kr.hhplus.be.server.infra.coupon.repository;

import kr.hhplus.be.server.infra.coupon.entity.CouponHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CouponHistoryJpaRepository extends JpaRepository<CouponHistory, Long> {

    @Query("""
        SELECT ch
          FROM Coupon c
          JOIN CouponHistory ch
            ON c.id = ch.coupon.id
    """)
    Optional<List<CouponHistory>> findCouponHistoryById(@Param("memberId") final Long memberId);
}
