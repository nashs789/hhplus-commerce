package kr.hhplus.be.server.domain.coupon.cache;

import kr.hhplus.be.server.domain.coupon.exception.CouponException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import static kr.hhplus.be.server.domain.coupon.cache.CouponApplyCache.APPLICANT_KEY_PREFIX;
import static kr.hhplus.be.server.domain.coupon.cache.CouponApplyCache.USER_KEY_PREFIX;
import static org.junit.jupiter.api.Assertions.*;

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
class CouponApplyCacheTest {

    final Long EXIST_COUPON_ID = 1L;
    final Long EXIST_MEMBER_ID1 = 1L;
    final Long EXIST_MEMBER_ID2 = 2L;
    final Long EXIST_MEMBER_ID3 = 3L;

    @Autowired
    private CouponCache couponCache;

    @Autowired
    private CouponApplyCache couponApplyCache;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @BeforeEach
    void setUp() {
        Objects.requireNonNull(redisTemplate.getConnectionFactory())
               .getConnection()
               .serverCommands()
               .flushDb();  // caches delete
        couponCache.initializeCache();  // cache warming
    }

    @Test
    @DisplayName("쿠폰 신청")
    void applyCoupon() {
        // when
        couponApplyCache.applyCoupon(EXIST_COUPON_ID, EXIST_MEMBER_ID1);

        // then
        assertNull(redisTemplate.opsForZSet()
                                .score(
                                        USER_KEY_PREFIX + EXIST_COUPON_ID,
                                        APPLICANT_KEY_PREFIX + EXIST_COUPON_ID
                                ));
    }

    @Test
    @DisplayName("쿠폰 중복 신청")
    void applyDuplicatedCoupon() {
        // given
        couponApplyCache.applyCoupon(EXIST_COUPON_ID, EXIST_MEMBER_ID1);

        // when
        CouponException couponException = assertThrows(CouponException.class, () -> {
            couponApplyCache.applyCoupon(EXIST_COUPON_ID, EXIST_MEMBER_ID1);
        });

        // then
        assertEquals(HttpStatus.BAD_REQUEST, couponException.getStatus());
        assertEquals("쿠폰 발급 대기중 입니다.", couponException.getMessage());
    }

    @Test
    @DisplayName("신청 시간 순서대로 신청자 조회")
    void getNextApplicant() {
        // given
        final List<Long> expectedList = List.of(EXIST_MEMBER_ID3, EXIST_MEMBER_ID2, EXIST_MEMBER_ID1);

        redisTemplate.opsForZSet()
                     .add(APPLICANT_KEY_PREFIX + EXIST_COUPON_ID,
                          EXIST_MEMBER_ID1.toString(),
                          Instant.now().toEpochMilli() + 200000L
                     );
        redisTemplate.opsForZSet()
                     .add(APPLICANT_KEY_PREFIX + EXIST_COUPON_ID,
                          EXIST_MEMBER_ID2.toString(),
                          Instant.now().toEpochMilli() + 10000L
                     );
        redisTemplate.opsForZSet()
                     .add(APPLICANT_KEY_PREFIX + EXIST_COUPON_ID,
                          EXIST_MEMBER_ID3.toString(),
                          Instant.now().toEpochMilli()
                     );

        // when && then
        for(Long memberId : expectedList) {
            assertEquals(memberId, couponApplyCache.getNextApplicant(EXIST_COUPON_ID));
        }
    }
}