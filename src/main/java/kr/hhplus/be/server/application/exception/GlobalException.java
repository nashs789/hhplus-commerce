package kr.hhplus.be.server.application.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(CommerceException.class)
    public ResponseEntity<String> handleException(CommerceException e) {
        return ResponseEntity.status(e.getStatus())
                             .body(e.getMessage());
    }
}
