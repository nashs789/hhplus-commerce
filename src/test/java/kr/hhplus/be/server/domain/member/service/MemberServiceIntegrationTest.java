package kr.hhplus.be.server.domain.member.service;

import kr.hhplus.be.server.domain.member.command.PointChargeCommand;
import kr.hhplus.be.server.domain.member.command.PointUseCommand;
import kr.hhplus.be.server.domain.member.exception.PointException;
import kr.hhplus.be.server.domain.member.info.MemberInfo;
import kr.hhplus.be.server.infra.member.entity.Member;
import kr.hhplus.be.server.infra.member.repository.MemberJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static kr.hhplus.be.server.infra.member.entity.PointHistory.PointUseType.CHARGE;
import static kr.hhplus.be.server.infra.member.entity.PointHistory.PointUseType.USE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@SpringBootTest
@Testcontainers
class MemberServiceIntegrationTest {

    private final Long BASE_POINT = 100_000L;
    private Member member;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @BeforeEach
    void setUp() {
        member = memberJpaRepository.save(Member.builder()
                                                .point(BASE_POINT)
                                                .build());
    }

    @Test
    @DisplayName("동시 충전 동시성 테스트")
    void chargeMemberPointConcurrencyTest() throws InterruptedException {
        // given
        final int TEST_CNT = 30;
        final long CHARGE_POINT = 10_000L;
        ExecutorService executor = Executors.newFixedThreadPool(30);
        CountDownLatch latch = new CountDownLatch(TEST_CNT);

        // when
        for(int i = 0; i < TEST_CNT; i++) {
            executor.submit(() -> {
                try {
                    memberService.chargeMemberPoint(PointChargeCommand.builder()
                                                                      .memberId(member.getId())
                                                                      .chargePoint(CHARGE_POINT)
                                                                      .pointUseType(CHARGE)
                                                                      .build());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        MemberInfo result = memberService.findMemberById(member.getId());

        // then
        final Long FINAL_POINT = BASE_POINT + TEST_CNT * CHARGE_POINT;

        assertEquals(FINAL_POINT, result.getPoint());
        assertEquals(0, latch.getCount());
    }

    @Test
    @DisplayName("동시 사용 동시성 테스트")
    void useMemberPointConcurrencyTest() throws InterruptedException {
        // given
        final int TEST_CNT = 10;
        final long USE_POINT = 10_000L;
        ExecutorService executor = Executors.newFixedThreadPool(30);
        CountDownLatch latch = new CountDownLatch(TEST_CNT);

        // when
        for(int i = 0; i < TEST_CNT; i++) {
            executor.submit(() -> {
                try {
                    memberService.useMemberPoint(PointUseCommand.builder()
                                                                .memberId(member.getId())
                                                                .usePoint(USE_POINT)
                                                                .pointUseType(USE)
                                                                .build());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        MemberInfo result = memberService.findMemberById(member.getId());

        // then
        assertEquals(0L, result.getPoint());
        assertEquals(0, latch.getCount());
    }

    @Test
    @DisplayName("포인트 사용 실패")
    void failUseMemberPoint() {
        // given
        final Long USE_POINT = BASE_POINT + 1L;

        // when
        PointException pointException = assertThrows(PointException.class, () ->
                memberService.useMemberPoint(PointUseCommand.builder()
                                                            .memberId(member.getId())
                                                            .usePoint(USE_POINT)
                                                            .pointUseType(USE)
                                                            .build())
        );

        // then
        assertEquals(PointException.class, pointException.getClass());
        assertEquals(BAD_REQUEST, pointException.getStatus());
        assertEquals("포인트가 부족 합니다.", pointException.getMessage());
    }
}