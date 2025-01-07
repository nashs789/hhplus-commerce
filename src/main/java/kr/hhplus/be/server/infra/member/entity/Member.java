package kr.hhplus.be.server.infra.member.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.member.info.MemberInfo;
import kr.hhplus.be.server.infra.common.entity.Timestamp;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table
public class Member extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private Long point;

    public MemberInfo toInfo() {
        return MemberInfo.builder()
                         .id(id)
                         .point(point)
                         .createAt(getCreatedAt())
                         .updateAt(getUpdatedAt())
                         .build();
    }
}
