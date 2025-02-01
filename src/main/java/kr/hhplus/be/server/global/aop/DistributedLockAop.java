package kr.hhplus.be.server.global.aop;

import kr.hhplus.be.server.global.lock.DistributedLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAop {

    private static final String REDISSON_LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient;
    private final AopTransaction transaction;
    private final ApplicationEventPublisher eventPublisher;

    @Around("@annotation(kr.hhplus.be.server.global.lock.DistributedLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock annotation = method.getAnnotation(DistributedLock.class);
        String key = REDISSON_LOCK_PREFIX + parseParameters(signature.getParameterNames(), joinPoint.getArgs(), annotation.key());
        RLock rLock = redissonClient.getLock(key);

        log.info("key = {}", key);

        try {
            if(!rLock.tryLock(annotation.waitTime(), annotation.leaseTime(), annotation.timeUnit())) {
                return false;
            }

            return annotation.openTx()
                 ? transaction.proceed(joinPoint)
                 : joinPoint.proceed();
        } catch(InterruptedException e) {
            throw new InterruptedException();
        } finally {
            try {
                if(TransactionSynchronizationManager.isActualTransactionActive()) {
                    eventPublisher.publishEvent(rLock);
                } else {
                    rLock.unlock();
                }
            } catch(IllegalMonitorStateException e) {
                log.info("Redisson Lock Already UnLock {} {}", method.getName(), key);
            }
        }
    }

    private Object parseParameters(String[] params, Object[] args, String key) {
        SpelExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();

        for (int i = 0; i < params.length; i++) {
            context.setVariable(params[i], args[i]);
        }

        return parser.parseExpression(key).getValue(context, Object.class);
    }
}
