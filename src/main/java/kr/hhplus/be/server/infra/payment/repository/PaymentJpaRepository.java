package kr.hhplus.be.server.infra.payment.repository;

import kr.hhplus.be.server.infra.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {

    @Query("""
        SELECT p
          FROM Payment p
         WHERE p.id = :paymentId
    """)
    Payment findOneById(@Param("paymentId") final Long paymentId);
}
