package kr.hhplus.be.server.domain.coupon.info;

import kr.hhplus.be.server.domain.coupon.exception.CouponException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CouponInfoUnitTest {

    @Test
    @DisplayName("쿠폰 발급 유효성 통과")
    void successPublishCoupon() {
        // given
        final Integer totalQuantity = 10;
        final Integer publishedQuantity = 0;
        LocalDateTime tomorrow = LocalDateTime.now()
                                              .plusDays(1);
        CouponInfo couponInfo = CouponInfo.builder()
                                          .totalQuantity(totalQuantity)
                                          .publishedQuantity(publishedQuantity)
                                          .expiredAt(tomorrow)
                                          .build();

        // when && then
        assertEquals(true, couponInfo.checkAvailability());
    }

    @Test
    @DisplayName("날짜가 만료된 쿠폰 발급 시도 실패")
    void failPublishExpiredCoupon() {
        // given
        final Integer totalQuantity = 10;
        final Integer publishedQuantity = 0;
        LocalDateTime yesterday = LocalDateTime.now()
                                               .minusDays(1);
        CouponInfo couponInfo = CouponInfo.builder()
                                          .totalQuantity(totalQuantity)
                                          .publishedQuantity(publishedQuantity)
                                          .expiredAt(yesterday)
                                          .build();

        // when && then
        assertEquals(false, couponInfo.checkAvailability());
    }

    @Test
    @DisplayName("발급 수량이 부족한 쿠폰 발급 시도 실패")
    void failPublishNotCapableCoupon() {
        // given
        final Integer totalQuantity = 10;
        final Integer publishedQuantity = 10;
        LocalDateTime tomorrow = LocalDateTime.now()
                                              .plusDays(1);
        CouponInfo couponInfo = CouponInfo.builder()
                                          .totalQuantity(totalQuantity)
                                          .publishedQuantity(publishedQuantity)
                                          .expiredAt(tomorrow)
                                          .build();

        // when && then
        assertEquals(false, couponInfo.checkAvailability());
    }
}