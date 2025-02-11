package kr.hhplus.be.server.domain.coupon.cache;

import jakarta.annotation.PostConstruct;
import kr.hhplus.be.server.domain.coupon.exception.CouponException;
import kr.hhplus.be.server.domain.coupon.info.CouponHistoryInfo;
import kr.hhplus.be.server.domain.coupon.repository.CouponRepository;
import kr.hhplus.be.server.infra.coupon.entity.key.CouponHistoryId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

import static kr.hhplus.be.server.domain.coupon.exception.CouponException.CouponExceptionCode.PUBLISH_DUPLICATED_COUPON;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponHistoryCache {

    public static final String COUPON_HISTORY_PREFIX = "COUPON-HISTORY-";

    private final StringRedisTemplate redisTemplate;
    private final CouponRepository couponRepository;

    @PostConstruct
    public void initializeCache() {
        Set<String> keys = redisTemplate.keys(COUPON_HISTORY_PREFIX + "*");

        if(!CollectionUtils.isEmpty(keys)) {
            redisTemplate.delete(keys);
        }
    }

    public void checkAppliedHistory(final Long couponId, final Long memberId) {
        final String key = COUPON_HISTORY_PREFIX + couponId;
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();

        if(Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
            List<String> couponIds = couponRepository.findCouponHistoryCouponId(couponId)
                                                     .stream()
                                                     .map(CouponHistoryInfo::getId)
                                                     .map(CouponHistoryId::getMemberId)
                                                     .map(String::valueOf)
                                                     .toList();

            for(String publishedCouponId : couponIds) {
                setOperations.add(key, publishedCouponId);
            }
        }

        if(Boolean.TRUE.equals(setOperations.isMember(key, memberId.toString()))) {
            throw new CouponException(PUBLISH_DUPLICATED_COUPON);
        }
    }

    public void addCouponHistory(final Long couponId, final Long memberId) {
        checkAppliedHistory(couponId, memberId);

        redisTemplate.opsForSet().add(COUPON_HISTORY_PREFIX + couponId, memberId.toString());
    }
}
