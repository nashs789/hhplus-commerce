package kr.hhplus.be.server.domain.coupon.exception;

import kr.hhplus.be.server.application.exception.CommerceException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

public class CouponException extends CommerceException {

    @Getter
    @RequiredArgsConstructor
    public enum CouponExceptionCode {
        NOT_EXIST_COUPON_HISTORY(HttpStatus.NO_CONTENT, "쿠폰 발급 정보가 없습니다."),
        NOT_VALID_COUPON(HttpStatus.BAD_REQUEST, "유효하지 않은 쿠폰 입니다.")
        ;

        private final HttpStatus status;
        private final String msg;
    }

    public CouponException(final CouponExceptionCode ex) {
        super(ex.getMsg(), ex.getStatus());
    }
}
