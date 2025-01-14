package kr.hhplus.be.server.domain.payment.exception;

import kr.hhplus.be.server.application.exception.CommerceException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

public class PaymentException extends CommerceException {

    @Getter
    @RequiredArgsConstructor
    public enum PaymentExceptionCode {
        FAIL_PAYMENT(HttpStatus.BAD_REQUEST, "결제에 실패 하였습니다.")
        ;

        private final HttpStatus status;
        private final String msg;
    }

    public PaymentException(final PaymentExceptionCode ex) {
        super(ex.getMsg(), ex.getStatus());
    }
}
