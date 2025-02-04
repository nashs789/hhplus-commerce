package kr.hhplus.be.server.domain.coupon.service;

import kr.hhplus.be.server.domain.coupon.exception.CouponException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;

import static kr.hhplus.be.server.domain.coupon.exception.CouponException.CouponExceptionCode.EXIST_COUPON_WAITING_QUEUE;

@Service
@RequiredArgsConstructor
public class CouponApplyCacheService {

    private static final String COUPON_KEY_PREFIX = "COUPON-APPLICANT-";
    private static final String USER_KEY_PREFIX = "USER-";

    private final RedisTemplate<String, String> redisTemplate;

    public void applyCoupon(final Long couponId, final Long memberId) {
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        final String redisCouponInfo = COUPON_KEY_PREFIX + couponId;
        final String redisUserInfo = USER_KEY_PREFIX + memberId;

        if(Objects.nonNull(zSetOperations.score(redisCouponInfo, redisUserInfo))) {
            throw new CouponException(EXIST_COUPON_WAITING_QUEUE);
        }

        zSetOperations.add(redisCouponInfo, redisUserInfo, Instant.now().toEpochMilli());
    }
}
