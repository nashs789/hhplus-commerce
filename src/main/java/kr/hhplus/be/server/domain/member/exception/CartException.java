package kr.hhplus.be.server.domain.member.exception;

import kr.hhplus.be.server.application.exception.CommerceException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class CartException extends CommerceException {

    @Getter
    @RequiredArgsConstructor
    public enum CartExceptionCode {
        EMPTY_CART(BAD_REQUEST, "장바구니가 비었습니다."),
        NO_SUCH_CART(BAD_REQUEST, "장바구니 정보가 없습니다."),
        FAIL_ADD_CART(BAD_REQUEST, "장바구니 상품 추가에 실패 했습니다."),
        FAIL_DELETE_CART(BAD_REQUEST, "장바구니에서 상품 삭제에 실패 했습니다.")
        ;

        private final HttpStatus status;
        private final String msg;
    }

    public CartException(final CartExceptionCode ex) {
        super(ex.getMsg(), ex.getStatus());
    }
}
