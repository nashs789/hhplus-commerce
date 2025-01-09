package kr.hhplus.be.server.infra.member.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.member.info.CartInfo;
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
public class Cart extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public CartInfo toInfo() {
        return CartInfo.builder()
                       .id(id)
                       .memberInfo(member.toInfo())
                       .build();
    }
}
