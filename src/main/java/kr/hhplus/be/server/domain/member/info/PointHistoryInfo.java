package kr.hhplus.be.server.domain.member.info;

import kr.hhplus.be.server.api.member.response.PointHistoryResponse;
import kr.hhplus.be.server.infra.member.entity.PointHistory.PointUseType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PointHistoryInfo {
    private Long id;
    private MemberInfo memberInfo;
    private PointUseType pointUseType;
    private Long beforeAmount;
    private Long afterAmount;

    public PointHistoryResponse toResponse() {
        return new PointHistoryResponse(
          id, memberInfo, pointUseType, beforeAmount, afterAmount
        );
    }
}
