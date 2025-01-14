package kr.hhplus.be.server.domain.product.exception;

import kr.hhplus.be.server.application.exception.CommerceException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

public class ProductException extends CommerceException {

    @Getter
    @RequiredArgsConstructor
    public enum ProductExceptionCode {
        NO_SUCH_PRODUCT(HttpStatus.BAD_REQUEST, "존재하지 않는 상품 입니다.")
        ;

        private final HttpStatus status;
        private final String msg;
    }

    public ProductException(final ProductExceptionCode ex) {
        super(ex.getMsg(), ex.getStatus());
    }
}
