package kr.hhplus.be.server.domain.payment.external;

import kr.hhplus.be.server.domain.payment.exception.PaymentException;
import org.springframework.stereotype.Component;

import static kr.hhplus.be.server.domain.payment.exception.PaymentException.PaymentExceptionCode.FAIL_PAYMENT;

@Component
public class FakePaymentSystemImpl implements PaymentSystem {

    @Override
    public boolean sendPayment(final Long memberId, final Long usePoint) {
        boolean result = true;

        if(!result) {
            throw new PaymentException(FAIL_PAYMENT);
        }

        return result;
    }
}
