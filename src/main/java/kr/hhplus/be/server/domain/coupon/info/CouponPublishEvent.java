package kr.hhplus.be.server.domain.coupon.info;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CouponPublishEvent {
    private final Long id;
}
