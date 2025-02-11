package kr.hhplus.be.server.domain.coupon.listener;

import kr.hhplus.be.server.domain.coupon.info.CouponPublishEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class CouponPublishEventListener {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePublishingCoupon(final CouponPublishEvent event) {
        log.info("쿠폰 발행 event after transaction: {}", event.getId());
    }
}
