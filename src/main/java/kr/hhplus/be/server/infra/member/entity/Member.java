package kr.hhplus.be.server.infra.member.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.member.info.MemberInfo;
import kr.hhplus.be.server.infra.common.entity.Timestamp;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table
public class Member extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long point;

    public static Member from(final MemberInfo info) {
        return Member.builder()
                     .id(info.getId())
                     .point(info.getPoint())
                     .build();
    }

    public MemberInfo toInfo() {
        return MemberInfo.builder()
                         .id(id)
                         .point(point)
                         .createAt(getCreatedAt())
                         .updateAt(getUpdatedAt())
                         .build();
    }
}
