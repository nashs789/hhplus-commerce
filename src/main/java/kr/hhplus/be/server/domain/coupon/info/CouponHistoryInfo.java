package kr.hhplus.be.server.domain.coupon.info;

import kr.hhplus.be.server.api.coupon.response.CouponHistoryResponse;
import kr.hhplus.be.server.domain.coupon.exception.CouponException;
import kr.hhplus.be.server.infra.coupon.entity.CouponHistory.CouponStatus;
import kr.hhplus.be.server.infra.coupon.entity.key.CouponHistoryId;
import lombok.Builder;
import lombok.Data;

import static kr.hhplus.be.server.domain.coupon.exception.CouponException.CouponExceptionCode.EXPIRED_COUPON;
import static kr.hhplus.be.server.domain.coupon.exception.CouponException.CouponExceptionCode.USED_COUPON;
import static kr.hhplus.be.server.infra.coupon.entity.CouponHistory.CouponStatus.EXPIRED;
import static kr.hhplus.be.server.infra.coupon.entity.CouponHistory.CouponStatus.USED;

@Data
@Builder
public class CouponHistoryInfo {
    private CouponHistoryId id;
    private CouponStatus status;

    public void useCoupon() {
        if(status == EXPIRED) {
            throw new CouponException(EXPIRED_COUPON);
        }

        if(status == USED) {
            throw new CouponException(USED_COUPON);
        }

        status = USED;
    }

    public CouponHistoryResponse toResponse() {
        return new CouponHistoryResponse(
                id.getCouponId(), id.getMemberId(), status
        );
    }
}
