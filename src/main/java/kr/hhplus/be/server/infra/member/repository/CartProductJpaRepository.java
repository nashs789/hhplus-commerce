package kr.hhplus.be.server.infra.member.repository;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.infra.member.entity.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartProductJpaRepository extends JpaRepository<CartProduct, Long> {

    @Query("""
        SELECT cp
          FROM Cart c
          JOIN CartProduct cp
            ON c.id = cp.cart.id
         WHERE c.member.id = :memberId
    """)
    Optional<List<CartProduct>> findCartProductsByMemberId(@Param("memberId") final Long memberId);

    @Query("""
        SELECT cp
          FROM Cart c
          JOIN CartProduct cp
            ON c.id = cp.cart.id
         WHERE c.member.id = :memberId
    """)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<List<CartProduct>> findCartProductsByMemberIdWithLock(@Param("memberId") final Long memberId);
}
