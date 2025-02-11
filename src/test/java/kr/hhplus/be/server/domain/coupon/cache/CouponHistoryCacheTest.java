package kr.hhplus.be.server.domain.coupon.cache;

import kr.hhplus.be.server.domain.coupon.exception.CouponException;
import kr.hhplus.be.server.domain.coupon.repository.CouponRepository;
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

import java.util.Objects;

import static kr.hhplus.be.server.domain.coupon.cache.CouponHistoryCache.COUPON_HISTORY_PREFIX;
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
class CouponHistoryCacheTest {

    final Long EXIST_MEMBER_ID = 1L;
    final Long DUPLICATE_APPLY_COUPON = 1L;
    final Long EXIST_COUPON_ID = 2L;

    final String EXIST_MEMBER_KEY = COUPON_HISTORY_PREFIX + EXIST_COUPON_ID;

    @Autowired
    private CouponCache couponCache;

    @Autowired
    private CouponHistoryCache couponHistoryCache;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private CouponRepository couponRepository;

    @BeforeEach
    void setUp() {
        Objects.requireNonNull(redisTemplate.getConnectionFactory())
               .getConnection()
               .serverCommands()
               .flushDb();  // caches delete
        couponCache.initializeCache();  // cache warming
    }

    @Test
    @DisplayName("쿠폰 이력 초기화")
    void deletePreviousCouponHistory() {
        // given
        redisTemplate.opsForSet().add(EXIST_MEMBER_KEY, EXIST_MEMBER_ID.toString());

        // when
        couponHistoryCache.initializeCache();

        // then
        assertNotEquals(Boolean.TRUE, redisTemplate.hasKey(EXIST_MEMBER_KEY));
    }

    @Test
    @DisplayName("쿠폰 발급 이력이 캐싱되어 있지 않는경우 캐싱이 되는지")
    void checkCouponHistory() {
        // given
        couponRepository.applyPublishedCoupon(EXIST_COUPON_ID, EXIST_MEMBER_ID);

        // when
        assertThrows(CouponException.class, () -> {
            couponHistoryCache.checkAppliedHistory(EXIST_COUPON_ID, EXIST_MEMBER_ID);
        });

        // then
        assertEquals(
                Boolean.TRUE,
                redisTemplate.opsForSet()
                             .isMember(COUPON_HISTORY_PREFIX + EXIST_COUPON_ID, EXIST_MEMBER_ID.toString())
        );
    }

    @Test
    @DisplayName("중복 쿠폰 이력 확인")
    void checkDuplicatedCouponHistory() {
        // when && then
        CouponException couponException = assertThrows(CouponException.class, () -> {
            couponHistoryCache.checkAppliedHistory(DUPLICATE_APPLY_COUPON, EXIST_MEMBER_ID);
        });

        // then
        assertEquals(HttpStatus.BAD_REQUEST, couponException.getStatus());
        assertEquals("쿠폰 중복 발급 신청 입니다.", couponException.getMessage());
    }

    @Test
    @DisplayName("쿠폰 이력 추가")
    void addCouponHistory() {
        // when
        couponHistoryCache.addCouponHistory(EXIST_COUPON_ID, EXIST_MEMBER_ID);

        // then
        assertEquals(Boolean.TRUE, redisTemplate.hasKey(EXIST_MEMBER_KEY));
    }
}