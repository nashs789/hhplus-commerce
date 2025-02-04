package kr.hhplus.be.server.domain.coupon.service;

import jakarta.annotation.PostConstruct;
import kr.hhplus.be.server.domain.coupon.info.CouponInfo;
import kr.hhplus.be.server.domain.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CouponCacheService {

    private static final String COUPON_KEY_PREFIX = "COUPON-INFO-";

    private final StringRedisTemplate redisTemplate;
    private final CouponRepository couponRepository;

    @PostConstruct
    public void initializeCache() {
        List<CouponInfo> appliableCoupons = couponRepository.findAll()
                                                            .stream()
                                                            .filter(CouponInfo::checkAvailability)
                                                            .toList();

        for(CouponInfo coupon : appliableCoupons) {
            addCouponCache(coupon.getId(), coupon.getRestCouponCount());
        }
    }

    public boolean isCouponExist(final Long couponId) {
        Boolean exist = redisTemplate.hasKey(COUPON_KEY_PREFIX + couponId);

        return Objects.nonNull(exist) && Boolean.TRUE.equals(exist);
    }

    public void addCouponCache(final Long couponId, final Integer couponCount) {
        redisTemplate.opsForValue().set(COUPON_KEY_PREFIX + couponId, String.valueOf(couponCount));
    }

    public long increaseCouponCount(final Long couponId) {
        return redisTemplate.opsForValue().increment(COUPON_KEY_PREFIX + couponId);
    }

    public long decreaseCouponCount(final Long couponId) {
        return redisTemplate.opsForValue().decrement(COUPON_KEY_PREFIX + couponId);
    }

    public int getCouponCount(final Long couponId) {
        String count = redisTemplate.opsForValue().get(COUPON_KEY_PREFIX + couponId);

        return Objects.nonNull(count) ? Integer.parseInt(count) : 0;
    }

    public boolean isAppliable(final Long couponId) {
        return isCouponExist(couponId) && getCouponCount(couponId) > 0;
    }
}
