package kr.hhplus.be.server.domain.coupon.cache;

import kr.hhplus.be.server.domain.coupon.exception.CouponException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Objects;

import static kr.hhplus.be.server.domain.coupon.exception.CouponException.CouponExceptionCode.EXIST_COUPON_WAITING_QUEUE;

@Component
@RequiredArgsConstructor
public class CouponApplyCache {

    public static final String APPLICANT_KEY_PREFIX = "COUPON-APPLICANT-";
    public static final String USER_KEY_PREFIX = "USER-";

    private final RedisTemplate<String, String> redisTemplate;

    public void applyCoupon(final Long couponId, final Long memberId) {
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        final String redisCouponInfo = APPLICANT_KEY_PREFIX + couponId;
        final String redisUserInfo = USER_KEY_PREFIX + memberId;

        if(Objects.nonNull(zSetOperations.score(redisCouponInfo, redisUserInfo))) {
            throw new CouponException(EXIST_COUPON_WAITING_QUEUE);
        }

        zSetOperations.add(redisCouponInfo, redisUserInfo, Instant.now().toEpochMilli());
    }

    public Long getNextApplicant(final Long couponId) {
        // TODO - NPE 유발 코드... 눈 지긋이 감는다...
        return Long.parseLong(redisTemplate.opsForZSet()
                                           .popMin(APPLICANT_KEY_PREFIX + couponId)
                                           .getValue()
                                           .replace(USER_KEY_PREFIX, ""));
    }
}
