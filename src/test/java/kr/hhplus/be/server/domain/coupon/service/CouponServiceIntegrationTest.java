package kr.hhplus.be.server.domain.coupon.service;

import kr.hhplus.be.server.domain.coupon.exception.CouponException;
import kr.hhplus.be.server.domain.coupon.info.CouponHistoryInfo;
import kr.hhplus.be.server.domain.coupon.info.CouponInfo;
import kr.hhplus.be.server.domain.member.info.MemberInfo;
import kr.hhplus.be.server.domain.member.service.MemberService;
import kr.hhplus.be.server.infra.coupon.entity.key.CouponHistoryId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Sql(
        scripts = {
                "/test-coupon-data.sql",
                "/test-member-data.sql",
                "/test-coupon-history-data.sql",
        },
        config = @SqlConfig(encoding = "UTF-8")
)
@SpringBootTest
@Testcontainers
class CouponServiceIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(CouponServiceIntegrationTest.class);
    final Long MEMBER_ID = 1L;
    final Long COUPON_ID = 1L;

    @Autowired
    MemberService memberService;

    @Autowired
    CouponService couponService;

    @Test
    @DisplayName("쿠폰 조회")
    @Transactional
    void findCouponById() {
        // given & when
        CouponInfo result = couponService.findByCouponId(COUPON_ID);

        // then
        assertEquals(COUPON_ID, result.getId());
    }

    @Test
    @DisplayName("쿠폰 히스토리 조회")
    @Transactional
    void findCouponHistoryByMemberId() {
        // given & when
        List<CouponHistoryInfo> result = couponService.findCouponHistoryMemberById(MEMBER_ID);

        // then
        assertEquals(1, result.size());
        Assertions.assertThat(result.stream()
                                    .map(CouponHistoryInfo::getId)
                                    .map(CouponHistoryId::getMemberId)
                                    .toList())
                  .containsExactly(MEMBER_ID);
    }

    @Test
    @DisplayName("쿠폰 중복 발급 실패")
    void failPublishDuplicatedCoupon() {
        // given & when
        CouponException couponException = assertThrows(CouponException.class, () -> {
            couponService.applyPublishedCoupon(COUPON_ID, memberService.findMemberById(MEMBER_ID));
        });

        // then
        assertEquals(BAD_REQUEST, couponException.getStatus());
        assertEquals("쿠폰 중복 발급 신청 입니다.", couponException.getMessage());
    }

    @Test
    @DisplayName("쿠폰 발급 완료")
    void test() {

    }
}