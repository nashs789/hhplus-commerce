package kr.hhplus.be.server.domain.member.exception;

import kr.hhplus.be.server.application.exception.CommerceException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

public class PointException extends CommerceException {

    @Getter
    @RequiredArgsConstructor
    public enum PointExceptionCode {
        FAIL_CHARGE(HttpStatus.BAD_REQUEST, "포인트 충전에 실패 했습니다."),
        FAIL_USE(HttpStatus.BAD_REQUEST, "포인트 사용에 실패 했습니다."),
        INSUFFICIENT_BALANCE(HttpStatus.BAD_REQUEST, "포인트가 부족 합니다.")
        ;

        private final HttpStatus status;
        private final String msg;
    }

    public PointException(final PointExceptionCode ex) {
        super(ex.getMsg(), ex.getStatus());
    }
}
