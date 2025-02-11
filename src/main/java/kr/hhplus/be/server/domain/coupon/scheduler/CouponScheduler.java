package kr.hhplus.be.server.domain.coupon.scheduler;

import kr.hhplus.be.server.domain.coupon.cache.CouponCache;
import kr.hhplus.be.server.domain.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponScheduler {

    private final CouponCache couponCache;
    private final CouponService couponService;

    @Scheduled(fixedDelay = 10_000)
    public void getApplicantAndPublishCoupon() {
        List<Long> couponIds = couponCache.getAppliedCouponIds();

        for(Long couponId : couponIds) {
            couponService.publishCoupon(couponId);
        }
    }
}
