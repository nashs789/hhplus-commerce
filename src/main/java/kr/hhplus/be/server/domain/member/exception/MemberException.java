package kr.hhplus.be.server.domain.member.exception;

import kr.hhplus.be.server.application.exception.CommerceException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

public class MemberException extends CommerceException {

    @Getter
    @RequiredArgsConstructor
    public enum MemberExceptionCode {
        NO_SUCH_MEMBER(HttpStatus.NO_CONTENT, "존재하지 않는 유저 정보 입니다.")
        ;

        private final HttpStatus status;
        private final String msg;
    }

    public MemberException(final MemberExceptionCode ex) {
        super(ex.getMsg(), ex.getStatus());
    }
}
