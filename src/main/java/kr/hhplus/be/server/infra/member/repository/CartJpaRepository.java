package kr.hhplus.be.server.infra.member.repository;

import kr.hhplus.be.server.infra.member.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartJpaRepository extends JpaRepository<Cart, Long> {

    @Query("""
        SELECT c
          FROM Cart c
         WHERE c.member.id = :memberId
    """)
    Optional<Cart> findCartByMemberId(@Param("memberId") final Long memberId);
}
