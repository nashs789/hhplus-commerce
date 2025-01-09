package kr.hhplus.be.server.domain.coupon.info;

import kr.hhplus.be.server.api.coupon.response.CouponHistoryResponse;
import kr.hhplus.be.server.domain.member.info.MemberInfo;
import kr.hhplus.be.server.infra.coupon.entity.CouponHistory.CouponStatus;
import kr.hhplus.be.server.infra.coupon.entity.CouponHistoryId;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CouponHistoryInfo {
    private CouponHistoryId id;
    private CouponInfo couponInfo;
    private MemberInfo memberInfo;
    private CouponStatus status;

    public CouponHistoryResponse toResponse() {
        return new CouponHistoryResponse(
                couponInfo, memberInfo, status
        );
    }
}
