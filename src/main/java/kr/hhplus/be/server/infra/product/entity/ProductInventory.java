package kr.hhplus.be.server.infra.product.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.product.info.ProductInfo;
import kr.hhplus.be.server.domain.product.info.ProductInventoryInfo;
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
public class ProductInventory extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long stock;

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public static ProductInventory from(ProductInventoryInfo info) {
        return ProductInventory.builder()
                               .id(info.getId())
                               .stock(info.getStock())
                               .product(Product.from(
                                       info.getProductInfo()
                               ))
                               .build();
    }

    public ProductInventoryInfo toInfo() {
        return ProductInventoryInfo.builder()
                                   .id(id)
                                   .stock(stock)
                                   .productInfo(product.toInfo())
                                   .build();
    }
}
