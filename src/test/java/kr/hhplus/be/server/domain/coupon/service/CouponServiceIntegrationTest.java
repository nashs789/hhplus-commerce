package kr.hhplus.be.server.domain.coupon.service;

import kr.hhplus.be.server.domain.coupon.exception.CouponException;
import kr.hhplus.be.server.domain.coupon.info.CouponHistoryInfo;
import kr.hhplus.be.server.domain.coupon.info.CouponInfo;
import kr.hhplus.be.server.domain.member.info.MemberInfo;
import kr.hhplus.be.server.domain.member.service.MemberService;
import kr.hhplus.be.server.infra.coupon.entity.Coupon;
import kr.hhplus.be.server.infra.coupon.entity.CouponHistory;
import kr.hhplus.be.server.infra.coupon.entity.key.CouponHistoryId;
import kr.hhplus.be.server.infra.coupon.repository.CouponHistoryJpaRepository;
import kr.hhplus.be.server.infra.coupon.repository.CouponJpaRepository;
import kr.hhplus.be.server.infra.member.entity.Member;
import kr.hhplus.be.server.infra.member.repository.MemberJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static kr.hhplus.be.server.infra.coupon.entity.CouponHistory.CouponStatus.NOT_USED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@SpringBootTest
@Testcontainers
class CouponServiceIntegrationTest {

    Member member;
    Coupon coupon;
    CouponHistory couponHistory;

    @Autowired
    MemberService memberService;

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Autowired
    CouponService couponService;

    @Autowired
    CouponJpaRepository couponJpaRepository;

    @Autowired
    CouponHistoryJpaRepository couponHistoryJpaRepository;

    @BeforeEach
    void setUp() {
        member = memberJpaRepository.save(Member.builder()
                                                .build());
        coupon = couponJpaRepository.save(Coupon.builder()
                                                .totalQuantity(10)
                                                .publishedQuantity(0)
                                                .code("TEST COUPON")
                                                .rate(5)
                                                .expiredAt(LocalDateTime.now().plusDays(1))
                                                .build());
        couponHistory = couponHistoryJpaRepository.save(CouponHistory.builder()
                                                                     .id(new CouponHistoryId(coupon.getId(), member.getId()))
                                                                     .member(member)
                                                                     .coupon(coupon)
                                                                     .status(NOT_USED)
                                                                     .build());
    }

    @Test
    @DisplayName("쿠폰 조회")
    @Transactional
    void findCouponById() {
        // given & when
        CouponInfo result = couponService.findCouponById(coupon.getId());

        // then
        assertEquals(coupon.getId(), result.getId());
    }

    @Test
    @DisplayName("쿠폰 히스토리 조회")
    @Transactional
    void findCouponHistoryByMemberId() {
        // given & when
        List<CouponHistoryInfo> result = couponService.findCouponHistoryMemberById(member.getId());

        // then
        assertEquals(1, result.size());
        Assertions.assertThat(result.stream()
                                      .map(CouponHistoryInfo::getMemberInfo)
                                      .map(MemberInfo::getId))
                  .containsExactly(member.getId());
    }

    @Test
    @DisplayName("쿠폰 중복 발급 실패")
    @Transactional
    void failPublishDuplicatedCoupon() {
        // given & when
        CouponException couponException = assertThrows(CouponException.class, () -> {
            couponService.applyPublishedCoupon(coupon.getId(), member.toInfo());
        });

        // then
        assertEquals(BAD_REQUEST, couponException.getStatus());
        assertEquals("쿠폰 중복 발급 신청 입니다.", couponException.getMessage());
    }

    @Test
    @DisplayName("쿠폰 발급 동시성 테스트")
    void publishCouponConcurrencyTest() throws InterruptedException {
        // given
        final int TEST_CNT = 30;
        final ExecutorService executor = Executors.newFixedThreadPool(TEST_CNT);
        final CountDownLatch succeedLatch = new CountDownLatch(10);
        final CountDownLatch failLatch = new CountDownLatch(20);
        List<MemberInfo> members = new ArrayList<>();

        for(int i = 0; i < TEST_CNT; i++) {
            members.add(memberService.saveMember(MemberInfo.builder()
                                                           .build()));
        }

        // when
        for(MemberInfo memberInfo : members) {
            executor.submit(() -> {
               try {
                   System.out.println(memberInfo);
                   couponService.applyPublishedCoupon(coupon.getId(), memberInfo);

                   succeedLatch.countDown();
               } catch(Exception e) {
                   failLatch.countDown();
               }
            });
        }

        succeedLatch.await();
        failLatch.await();

        // then
        assertEquals(0, succeedLatch.getCount());
        assertEquals(0, failLatch.getCount());
    }
}