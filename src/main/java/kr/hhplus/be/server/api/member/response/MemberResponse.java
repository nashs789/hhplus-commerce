package kr.hhplus.be.server.api.member.response;

import java.time.LocalDateTime;

public record MemberResponse(
        Long userId,
        Long point,
        LocalDateTime createAt,
        LocalDateTime updateAt
) {
}
