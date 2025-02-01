package kr.hhplus.be.server.global.listener;

import org.redisson.api.RLock;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ReleaseLockListener {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void releaseLock(RLock rLock) {
        rLock.unlock();
    }
}
