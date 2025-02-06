package kr.hhplus.be.server.domain.coupon.info;

import kr.hhplus.be.server.domain.coupon.exception.CouponException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

class CouponInfoTest {

    @DisplayName("쿠폰 발급 수량에 따른 신청 성공 테스트")
    @ParameterizedTest(name = "추가 발급 가능 테스트[{0}]")
    @ValueSource(ints = {0, 1, 5, 10, 29})
    void successCheckAvailabilityForQuantity(final int publishedQuantity) {
        // given
        final int TOTAL_QUANTITY = 30;
        CouponInfo couponInfo = CouponInfo.builder()
                                          .totalQuantity(TOTAL_QUANTITY)
                                          .publishedQuantity(publishedQuantity)
                                          .expiredAt(LocalDateTime.now().plusDays(1))
                                          .build();

        // when & then
        assertEquals(true, couponInfo.checkAvailability());
    }

    @DisplayName("쿠폰 발급 수량에 따른 신청 실패 유효성 테스트")
    @ParameterizedTest(name = "발급 수량 초과 유효성 테스트[{0}]")
    @ValueSource(ints = {30, 31, 100})
    void failCheckAvailabilityForQuantity(final int publishedQuantity) {
        // given
        final int TOTAL_QUANTITY = 30;
        CouponInfo couponInfo = CouponInfo.builder()
                                          .totalQuantity(TOTAL_QUANTITY)
                                          .publishedQuantity(publishedQuantity)
                                          .expiredAt(LocalDateTime.now().plusDays(1))
                                          .build();

        // when && then
        assertEquals(false, couponInfo.checkAvailability());
    }
}