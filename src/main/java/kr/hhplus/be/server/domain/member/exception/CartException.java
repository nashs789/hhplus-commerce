package kr.hhplus.be.server.domain.member.exception;

import kr.hhplus.be.server.application.exception.CommerceException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

public class CartException extends CommerceException {

    @Getter
    @RequiredArgsConstructor
    public enum CartExceptionCode {
        NO_SUCH_CART(HttpStatus.BAD_REQUEST, "장바구니 정보가 없습니다."),
        FAIL_ADD_CART(HttpStatus.BAD_REQUEST, "장바구니 상품 추가에 실패 했습니다."),
        FAIL_DELETE_CART(HttpStatus.BAD_REQUEST, "장바구니에서 상품 삭제에 실패 했습니다.")
        ;

        private final HttpStatus status;
        private final String msg;
    }

    public CartException(final CartExceptionCode ex) {
        super(ex.getMsg(), ex.getStatus());
    }
}
