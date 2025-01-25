package kr.hhplus.be.server.domain.coupon.info;

import kr.hhplus.be.server.domain.coupon.exception.CouponException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kr.hhplus.be.server.infra.coupon.entity.CouponHistory.CouponStatus.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

class CouponHistoryInfoTest {

    @Test
    @DisplayName("정상 쿠폰 사용 시도")
    void useValidCoupon() {
        // given
        final CouponHistoryInfo info = CouponHistoryInfo.builder()
                                                        .status(NOT_USED)
                                                        .build();

        // when & then
        assertDoesNotThrow(info::useCoupon);
    }

    @Test
    @DisplayName("만료된 쿠폰 사용 시도")
    void useExpiredCoupon() {
        // given
        final CouponHistoryInfo info = CouponHistoryInfo.builder()
                                                        .status(EXPIRED)
                                                        .build();

        // when
        CouponException couponException = assertThrows(CouponException.class, info::useCoupon);

        // then
        assertEquals(BAD_REQUEST, couponException.getStatus());
        assertEquals("만료된 쿠폰 입니다.", couponException.getMessage());
    }

    @Test
    @DisplayName("이미 사용된 쿠폰 사용 시도")
    void useUsedCoupon() {
        // given
        final CouponHistoryInfo info = CouponHistoryInfo.builder()
                                                        .status(USED)
                                                        .build();

        // when
        CouponException couponException = assertThrows(CouponException.class, info::useCoupon);

        // then
        assertEquals(BAD_REQUEST, couponException.getStatus());
        assertEquals("이미 사용된 쿠폰 입니다.", couponException.getMessage());
    }
}