package kr.hhplus.be.server.infra.member.repository;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.infra.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    @Query("""
        SELECT m
          FROM Member m
         WHERE m.id = :memberId
    """)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Member> findByMemberIdWithLock(@Param("memberId") final Long memberId);

    @Modifying
    @Query("""
        UPDATE Member m
           SET m.point = :point
         WHERE m.id = :id
    """)
    int chargeMemberPoint(@Param("id") Long id, @Param("point") Long point);

    @Modifying
    @Query("""
        UPDATE Member m
           SET m.point = :point
         WHERE m.id = :id
    """)
    int useMemberPoint(@Param("id") Long id, @Param("point") Long point);
}
