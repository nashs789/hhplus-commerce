package kr.hhplus.be.server.infra.order.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.infra.common.entity.Timestamp;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "orders")
public class Order extends Timestamp {

    @Getter
    @RequiredArgsConstructor
    public enum OrderStatus {
        NOT_PAYED,
        PAYED,
        REFUND,
        CANCEL
    }

    @Getter
    @RequiredArgsConstructor
    public enum OrderShipStatus {
        SHIPPING,
        SHIPPED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private Long quantity;

    @Column
    private Long finalPrice;

    @Column
    private Long originalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    private OrderShipStatus orderShipStatus;
}
