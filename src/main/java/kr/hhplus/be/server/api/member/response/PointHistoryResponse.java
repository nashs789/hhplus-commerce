package kr.hhplus.be.server.api.member.response;

import kr.hhplus.be.server.domain.member.info.MemberInfo;
import kr.hhplus.be.server.infra.member.entity.PointHistory.PointUseType;

public record PointHistoryResponse(
        Long id,
        MemberInfo member,
        PointUseType pointUseType,
        Long beforeAmount,
        Long afterAmount
) {
}
