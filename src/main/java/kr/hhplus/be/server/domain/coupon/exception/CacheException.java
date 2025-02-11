package kr.hhplus.be.server.domain.coupon.exception;

import kr.hhplus.be.server.application.exception.CommerceException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class CacheException extends CommerceException {

    @Getter
    @RequiredArgsConstructor
    public enum CacheExceptionCode {
        COUPON_DUPLICATED_APPLY(INTERNAL_SERVER_ERROR, "이미 발급 받으신 쿠폰 입니다."),
        ;

        private final HttpStatus status;
        private final String msg;
    }

    public CacheException(final CacheExceptionCode ex) {
        super(ex.getMsg(), ex.getStatus());
    }
}
