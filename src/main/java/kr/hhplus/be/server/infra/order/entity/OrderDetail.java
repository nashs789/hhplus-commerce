package kr.hhplus.be.server.infra.order.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.order.info.OrderDetailInfo;
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
public class OrderDetail extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column
    private Long quantity;

    public void setOrder(final Order order) {
        this.order = order;
    }

    public static OrderDetail of(final OrderDetailInfo orderDetailInfo) {
        return OrderDetail.builder()
                          .id(orderDetailInfo.getId())
                          .product(Product.of(orderDetailInfo.getProductInfo()))
                          .build();
    }

    public OrderDetailInfo toInfo() {
        return OrderDetailInfo.builder()
                              .id(id)
                              .quantity(quantity)
                              .productInfo(product.toInfo())
                              .build();
    }
}
