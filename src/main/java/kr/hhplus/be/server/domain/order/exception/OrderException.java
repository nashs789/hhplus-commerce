package kr.hhplus.be.server.domain.order.exception;

import kr.hhplus.be.server.application.exception.CommerceException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

public class OrderException extends CommerceException {

    @Getter
    @RequiredArgsConstructor
    public enum OrderExceptionCode {
        NO_SUCH_ORDER(HttpStatus.BAD_REQUEST, "존재하지 않는 주문 입니다.")
        ;

        private final HttpStatus status;
        private final String msg;
    }

    public OrderException(final OrderExceptionCode ex) {
        super(ex.getMsg(), ex.getStatus());
    }
}
