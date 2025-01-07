package kr.hhplus.be.server.infra.member.repository;

import kr.hhplus.be.server.infra.member.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryJpaRepository extends JpaRepository<PointHistory, Long> {
}
