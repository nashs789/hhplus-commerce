package kr.hhplus.be.server.application.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CommerceException extends RuntimeException{

    private HttpStatus status;

    public CommerceException(final String msg, final HttpStatus status) {
        super(msg);
        this.status = status;
    }
}
