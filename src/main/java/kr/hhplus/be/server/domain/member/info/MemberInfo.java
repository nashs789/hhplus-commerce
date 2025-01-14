package kr.hhplus.be.server.domain.member.info;

import kr.hhplus.be.server.api.member.response.MemberResponse;
import kr.hhplus.be.server.domain.member.exception.PointException;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

import static kr.hhplus.be.server.domain.member.exception.PointException.PointExceptionCode.INSUFFICIENT_BALANCE;

@Data
@Builder
public class MemberInfo {
    private Long id;
    private Long point;
    private Long afterPoint;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public void chargePoint(final Long chargePoint) {
        afterPoint = point + chargePoint;
    }

    public void usePoint(final Long usePoint) {
        afterPoint = point - usePoint;

        if(afterPoint < 0) {
            throw new PointException(INSUFFICIENT_BALANCE);
        }
    }

    public void isAffordable(final Long orderPrice) {
        if(point < orderPrice) {
            throw new PointException(INSUFFICIENT_BALANCE);
        }
    }

    public MemberResponse toResponse() {
        return new MemberResponse(id, point, createAt, updateAt);
    }
}
