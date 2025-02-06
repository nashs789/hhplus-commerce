package kr.hhplus.be.server.domain.coupon.cache;

import kr.hhplus.be.server.domain.coupon.info.CouponInfo;
import kr.hhplus.be.server.domain.coupon.repository.CouponRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import static kr.hhplus.be.server.domain.coupon.cache.CouponApplyCache.APPLICANT_KEY_PREFIX;
import static kr.hhplus.be.server.domain.coupon.cache.CouponCache.COUPON_KEY_PREFIX;
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
class CouponCacheTest {

    final Long MYSTERY_COUPON_ID = Long.MAX_VALUE;
    final Long NEW_COUPON_ID = Long.MIN_VALUE;
    final Long EXIST_COUPON_ID = 1L;
    final String MYSTERY_COUPON_KEY = COUPON_KEY_PREFIX + MYSTERY_COUPON_ID;
    final String NEW_COUPON_KEY = COUPON_KEY_PREFIX + NEW_COUPON_ID;

    @Autowired
    private CouponCache couponCache;

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
    @DisplayName("모든 쿠폰 캐시 초기화")
    void initAllCouponToCache() {
        // given
        final List<Long> couponIds = couponRepository.findAll()
                                                     .stream()
                                                     .map(CouponInfo::getId)
                                                     .toList();

        // when && then
        for(Long couponId : couponIds) {
            Boolean exist = redisTemplate.hasKey(COUPON_KEY_PREFIX + couponId);

            assertTrue(
                    Objects.nonNull(exist) && Boolean.TRUE.equals(exist)
            );
        }
    }

    @Test
    @DisplayName("쿠폰 캐싱 여부")
    void isCouponInCache() {
        // when && then
        assertFalse(couponCache.isCouponExist(MYSTERY_COUPON_ID));
        assertTrue(couponCache.isCouponExist(EXIST_COUPON_ID));
    }

    @Test
    @DisplayName("쿠폰 정보 캐시 등록")
    void addCouponToCache() {
        // given
        redisTemplate.opsForValue().set(NEW_COUPON_KEY, "0");

        // when && then
        assertTrue(couponCache.isCouponExist(NEW_COUPON_ID));
    }

    @Test
    @DisplayName("쿠폰 잔량 감소")
    void decreaseCouponCount() {
        // given
        redisTemplate.opsForValue().set(NEW_COUPON_KEY, "1");

        // when
        couponCache.decreaseCouponCount(NEW_COUPON_ID);

        Boolean exist = redisTemplate.hasKey(NEW_COUPON_KEY);
        String count = redisTemplate.opsForValue().get(NEW_COUPON_KEY);

        Objects.requireNonNull(exist);
        Objects.requireNonNull(count);

        // then
        assertTrue(exist);
        assertEquals(0, Integer.parseInt(count));
    }

    @Test
    @DisplayName("쿠폰 잔량 조회")
    void getCouponFromCache() {
        // given
        final List<CouponInfo> couponInfos = couponRepository.findAll();

        // when && then
        for(CouponInfo couponInfo: couponInfos) {
            int couponCount = couponCache.getCouponCount(couponInfo.getId());

            assertEquals(couponInfo.getRestCouponCount(), couponCount);
        }
    }

    @Test
    @DisplayName("쿠폰 이용 가능 여부")
    void isCouponAppliable() {
        // given
        final Long ANOTHER_NEW_COUPON_ID = NEW_COUPON_ID - 1;

        redisTemplate.opsForValue().set(MYSTERY_COUPON_KEY, "100");
        redisTemplate.opsForValue().set(NEW_COUPON_KEY, "100");
        redisTemplate.opsForValue().set(COUPON_KEY_PREFIX + ANOTHER_NEW_COUPON_ID, "0");

        // then && when
        assertFalse(couponCache.isAppliable(MYSTERY_COUPON_ID));
        assertTrue(couponCache.isAppliable(NEW_COUPON_ID));
        assertFalse(couponCache.isAppliable(ANOTHER_NEW_COUPON_ID));
    }

    @Test
    @DisplayName("캐싱된 쿠폰 아이디 리스트 조회")
    void getAllCouponIds() {
        // given
        final List<Long> couponIds = couponRepository.findAll()
                                                     .stream()
                                                     .map(CouponInfo::getId)
                                                     .toList();

        for(Long couponId : couponIds) {
            redisTemplate.opsForValue().set(APPLICANT_KEY_PREFIX + couponId, "memberId");
        }

        // when
        final List<Long> cachingIds = couponCache.getAppliedCouponIds();

        // then
        Assertions.assertThat(couponIds)
                  .containsExactlyInAnyOrderElementsOf(cachingIds);
    }
}