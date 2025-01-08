package kr.hhplus.be.server.infra.product.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.product.info.ProductInfo;
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
public class Product extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @Column
    private Long price;

    @Column
    private String image;

    public ProductInfo toInfo() {
        return ProductInfo.builder()
                          .id(id)
                          .name(name)
                          .price(price)
                          .image(image)
                          .build();
    }
}
