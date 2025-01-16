package kr.hhplus.be.server.infra.coupon.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.coupon.info.CouponHistoryInfo;
import kr.hhplus.be.server.infra.common.entity.Timestamp;
import kr.hhplus.be.server.infra.coupon.entity.key.CouponHistoryId;
import kr.hhplus.be.server.infra.member.entity.Member;
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

    @ManyToOne
    @MapsId("couponId")
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne
    @MapsId("memberId")
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private CouponStatus status;

    public CouponHistoryInfo toInfo() {
        return CouponHistoryInfo.builder()
                                .id(id)
                                .couponInfo(coupon.toInfo())
                                .memberInfo(member.toInfo())
                                .status(status)
                                .build();
    }
}
