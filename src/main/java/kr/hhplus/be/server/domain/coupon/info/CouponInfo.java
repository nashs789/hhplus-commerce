package kr.hhplus.be.server.domain.coupon.info;

import kr.hhplus.be.server.api.coupon.response.CouponResponse;
import kr.hhplus.be.server.domain.coupon.exception.CouponException;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static kr.hhplus.be.server.domain.coupon.exception.CouponException.CouponExceptionCode.NOT_VALID_COUPON;

@Data
@Builder
public class CouponInfo {
    private Long id;
    private String code;
    private Integer rate;
    private Integer totalQuantity;
    private Integer publishedQuantity;
    private LocalDateTime expiredAt;

    public void checkAvailability() {
        LocalDate now = LocalDate.now();

        // 기간 만료 || 쿠폰 수 부족
        if(now.isAfter(expiredAt.toLocalDate())
        || this.totalQuantity <= this.publishedQuantity) {
            throw new CouponException(NOT_VALID_COUPON);
        }

        publishedQuantity += 1;
    }

    public CouponResponse toResponse(){
        return new CouponResponse(
          id, code, rate, totalQuantity, publishedQuantity, expiredAt
        );
    }
}
