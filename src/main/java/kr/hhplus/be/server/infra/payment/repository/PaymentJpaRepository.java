package kr.hhplus.be.server.infra.payment.repository;

import kr.hhplus.be.server.infra.payment.entity.Payment;
import kr.hhplus.be.server.infra.payment.entity.Payment.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {

    @Query("""
        SELECT p
          FROM Payment p
         WHERE p.id = :paymentId
    """)
    Payment findOneById(@Param("paymentId") final Long paymentId);

    @Query("""
        SELECT p
          FROM Payment p
         WHERE p.paymentStatus = :paymentStatus
           AND p.createdAt BETWEEN :startDate AND :endDate
    """)
    List<Payment> findCurrentThreeDaysPayment(
            @Param("startDate") LocalDateTime threeDaysBefore,
            @Param("endDate") LocalDateTime now,
            @Param("paymentStatus") PaymentStatus paymentStatus);
}
