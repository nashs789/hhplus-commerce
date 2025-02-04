package kr.hhplus.be.server.domain.coupon.info;

import kr.hhplus.be.server.api.coupon.response.CouponResponse;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class CouponInfo {
    private Long id;
    private String code;
    private Integer rate;
    private Integer totalQuantity;
    private Integer publishedQuantity;
    private LocalDateTime expiredAt;

    public boolean checkAvailability() {
        // 기간 ok && 쿠폰 수 ok
        return LocalDate.now().isBefore(expiredAt.toLocalDate()) && this.totalQuantity > this.publishedQuantity;
    }

    public int getRestCouponCount() {
        return totalQuantity - publishedQuantity;
    }

    public CouponResponse toResponse(){
        return new CouponResponse(
          id, code, rate, totalQuantity, publishedQuantity, expiredAt
        );
    }
}
