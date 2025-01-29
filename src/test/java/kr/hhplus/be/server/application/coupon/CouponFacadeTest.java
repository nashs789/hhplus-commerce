package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.member.info.MemberInfo;
import kr.hhplus.be.server.domain.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
class CouponFacadeTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private CouponFacade couponFacade;

    @Test
    @DisplayName("쿠폰 발급 동시성 테스트")
    void tryApplyDuplicatedCoupon() throws InterruptedException {
        // given
        final int TEST_CNT = 60;
        final ExecutorService executor = Executors.newFixedThreadPool(TEST_CNT);
        final CountDownLatch succeedLatch = new CountDownLatch(30);
        final CountDownLatch failLatch = new CountDownLatch(30);

        // when
        for(long i = 1; i <= TEST_CNT; i++) {
            final long memberId = i;

            executor.submit(() -> {
                try {
                    MemberInfo member = memberService.findMemberById(memberId);
                    couponFacade.applyCouponById(2L, member.getId());

                    succeedLatch.countDown();
                } catch(Exception e) {
                    failLatch.countDown();
                }
            });
        }

        succeedLatch.await();
        failLatch.await();

        // then
        assertEquals(0, succeedLatch.getCount());
        assertEquals(0, failLatch.getCount());
    }
}