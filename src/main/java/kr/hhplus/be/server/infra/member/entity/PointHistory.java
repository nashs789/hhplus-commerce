package kr.hhplus.be.server.infra.member.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.member.info.PointHistoryInfo;
import kr.hhplus.be.server.infra.common.entity.Timestamp;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table
public class PointHistory extends Timestamp {

    @Getter
    @RequiredArgsConstructor
    public enum PointUseType {
        CHARGE,
        USE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    private PointUseType pointUseType;

    @Column
    private Long beforeAmount;

    @Column
    private Long afterAmount;

    public PointHistoryInfo toInfo() {
        return PointHistoryInfo.builder()
                               .id(id)
                               .memberInfo(member.toInfo())
                               .pointUseType(pointUseType)
                               .beforeAmount(beforeAmount)
                               .afterAmount(afterAmount)
                               .build();
    }
}
