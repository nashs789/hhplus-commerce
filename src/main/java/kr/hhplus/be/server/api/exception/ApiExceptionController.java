package kr.hhplus.be.server.api.exception;

import kr.hhplus.be.server.application.exception.CommerceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionController {

    @ExceptionHandler(CommerceException.class)
    public ResponseEntity<String> handleException(CommerceException e) {
        return ResponseEntity.status(e.getStatus())
                             .body(e.getMessage());
    }
}
