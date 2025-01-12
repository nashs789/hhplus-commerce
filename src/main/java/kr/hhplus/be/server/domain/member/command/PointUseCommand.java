package kr.hhplus.be.server.domain.member.command;

import kr.hhplus.be.server.infra.member.entity.PointHistory.PointUseType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PointUseCommand {
    private Long memberId;
    private Long usePoint;
    private PointUseType pointUseType;
}
