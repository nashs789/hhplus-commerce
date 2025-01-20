package kr.hhplus.be.server.application.coupon;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class CouponFacadeIntegrationTest {

    @Test
    @DisplayName("쿠폰 중복 발급 실패")
    void failPublishDuplicatedCoupon() {
        // given

        // when
        //couponService.applyPublishedCoupon()

        // then
    }

    @Test
    @DisplayName("쿠폰 발급 동시성 테스트")
    void test2() {

    }
}