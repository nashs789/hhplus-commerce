package kr.hhplus.be.server.api.member.request;

import jakarta.validation.constraints.NotNull;

public record PointUseRequest(
        @NotNull(message = "사용 금액은 필수 입니다.")
        Long usePoint
) {
}
