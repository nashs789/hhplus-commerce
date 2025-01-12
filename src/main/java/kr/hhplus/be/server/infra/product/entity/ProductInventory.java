package kr.hhplus.be.server.infra.product.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.product.info.ProductInventoryInfo;
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
public class ProductInventory extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private Long stock;

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public ProductInventoryInfo toInfo() {
        return ProductInventoryInfo.builder()
                                   .id(id)
                                   .stock(stock)
                                   .productInfo(product.toInfo())
                                   .build();
    }
}
