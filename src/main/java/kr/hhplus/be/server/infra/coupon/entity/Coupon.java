package kr.hhplus.be.server.infra.coupon.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.coupon.info.CouponInfo;
import kr.hhplus.be.server.infra.common.entity.Timestamp;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table
public class Coupon extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String code;

    @Column
    private Integer rate;

    @Column
    private Integer totalQuantity;

    @Column
    private Integer publishedQuantity;

    @Column
    LocalDateTime expiredAt;

    public CouponInfo toInfo() {
        return CouponInfo.builder()
                         .id(id)
                         .code(code)
                         .rate(rate)
                         .totalQuantity(totalQuantity)
                         .publishedQuantity(publishedQuantity)
                         .expiredAt(expiredAt)
                         .build();
    }
}
