package kr.hhplus.be.server.infra.coupon.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.coupon.info.CouponHistoryInfo;
import kr.hhplus.be.server.infra.common.entity.Timestamp;
import kr.hhplus.be.server.infra.coupon.entity.key.CouponHistoryId;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table
public class CouponHistory extends Timestamp {

    @Getter
    @RequiredArgsConstructor
    public enum CouponStatus {
        USED,
        NOT_USED,
        EXPIRED
    }

    @EmbeddedId
    private CouponHistoryId id;

    @Enumerated(EnumType.STRING)
    private CouponStatus status;

    public static CouponHistory create(final Long couponId, final Long memberId) {
        return CouponHistory.builder()
                            .id(new CouponHistoryId(couponId, memberId))
                            .status(CouponStatus.NOT_USED)
                            .build();
    }

    public CouponHistoryInfo toInfo() {
        return CouponHistoryInfo.builder()
                                .id(id)
                                .status(status)
                                .build();
    }
}
