package kr.hhplus.be.server.api.member.request;

import jakarta.validation.constraints.NotNull;

public record PointChargeRequest(
        @NotNull(message = "충전 금액은 필수 입니다.")
        Long chargePoint
) {
}
