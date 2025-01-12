package kr.hhplus.be.server.infra.coupon.entity.key;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CouponHistoryId implements Serializable {
    private Long couponId;
    private Long memberId;
}
