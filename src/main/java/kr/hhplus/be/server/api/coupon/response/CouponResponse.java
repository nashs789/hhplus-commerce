package kr.hhplus.be.server.api.coupon.response;

import java.time.LocalDateTime;

public record CouponResponse(
        Long id,
        String code,
        Integer rate,
        Integer totalQuantity,
        Integer publishedQuantity,
        LocalDateTime expiredAt
) {
}
