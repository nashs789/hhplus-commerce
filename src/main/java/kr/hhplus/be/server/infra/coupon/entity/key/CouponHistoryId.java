package kr.hhplus.be.server.infra.coupon.entity.key;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@ToString
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CouponHistoryId implements Serializable {
    private Long couponId;
    private Long memberId;
}
