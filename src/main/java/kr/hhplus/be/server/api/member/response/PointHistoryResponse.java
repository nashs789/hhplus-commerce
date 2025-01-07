package kr.hhplus.be.server.api.member.response;

import kr.hhplus.be.server.infra.member.entity.Member;
import kr.hhplus.be.server.infra.member.entity.PointHistory.PointUseType;

public record PointHistoryResponse(
        Long id,
        Member member,
        PointUseType pointUseType,
        Long beforeAmount,
        Long afterAmount
) {
}
