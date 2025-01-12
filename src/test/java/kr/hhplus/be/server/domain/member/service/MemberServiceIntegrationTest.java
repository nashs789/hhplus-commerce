package kr.hhplus.be.server.domain.member.service;

import kr.hhplus.be.server.domain.member.repository.MemberRepository;
import kr.hhplus.be.server.infra.member.entity.Member;
import kr.hhplus.be.server.infra.member.repository.MemberJpaRepository;
import kr.hhplus.be.server.infra.member.repository.impl.MemberRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

// TODO - 통합 테스트 작성 해야하는데 오류 해결 해야겠는데...?

@SpringBootTest
class MemberServiceIntegrationTest {

    private Member member;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @BeforeEach
    void setUp() {
        member = memberJpaRepository.save(Member.builder()
                                                .point(10_000L)
                                                .build());
    }

    @Test
    @DisplayName("동시 충전 동시성 테스트")
    void chargeMemberPointConcurrencyTest() {
    }

    @Test
    @DisplayName("포인트 충전 실패")
    void failChargeMemberPoint() {
    }

    @Test
    @DisplayName("동시 사용 동시성 테스트")
    void useMemberPointConcurrencyTest() {
    }

    @Test
    @DisplayName("포인트 사용 실패")
    void failUseMemberPoint() {
    }
}