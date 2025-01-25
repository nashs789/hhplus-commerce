package kr.hhplus.be.server.domain.product.exception;

import kr.hhplus.be.server.application.exception.CommerceException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class ProductException extends CommerceException {

    @Getter
    @RequiredArgsConstructor
    public enum ProductExceptionCode {
        NO_SUCH_PRODUCT(BAD_REQUEST, "존재하지 않는 상품 입니다."),
        INSUFFICIENT_STOCK(BAD_REQUEST, "재고가 부족 합니다.")
        ;

        private final HttpStatus status;
        private final String msg;
    }

    public ProductException(final ProductExceptionCode ex) {
        super(ex.getMsg(), ex.getStatus());
    }
}
