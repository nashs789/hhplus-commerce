package kr.hhplus.be.server.infra.product.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.product.info.ProductInfo;
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
public class Product extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private Long price;

    @Column
    private String image;

    public static Product create(ProductInfo info) {
        return Product.builder()
                      .id(info.getId())
                      .name(info.getName())
                      .price(info.getPrice())
                      .image(info.getImage())
                      .createdAt(info.getCreatedAt())
                      .updatedAt(info.getUpdatedAt())
                      .build();
    }

    public ProductInfo toInfo() {
        return ProductInfo.builder()
                          .id(id)
                          .name(name)
                          .price(price)
                          .image(image)
                          .createdAt(getCreatedAt())
                          .updatedAt(getUpdatedAt())
                          .build();
    }
}
