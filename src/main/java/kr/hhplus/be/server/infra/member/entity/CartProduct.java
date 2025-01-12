package kr.hhplus.be.server.infra.member.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.member.info.CartProductInfo;
import kr.hhplus.be.server.infra.common.entity.Timestamp;
import kr.hhplus.be.server.infra.product.entity.Product;
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
public class CartProduct extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column
    private Long quantity;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    public CartProductInfo toInfo() {
        return CartProductInfo.builder()
                              .id(id)
                              .productInfo(product.toInfo())
                              .quantity(quantity)
                              .cartInfo(cart.toInfo())
                              .build();
    }
}
