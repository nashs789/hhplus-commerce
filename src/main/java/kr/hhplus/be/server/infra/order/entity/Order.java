package kr.hhplus.be.server.infra.order.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.order.info.OrderInfo;
import kr.hhplus.be.server.infra.common.entity.Timestamp;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

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
        NOT_DEPARTURE,
        SHIPPING,
        SHIPPED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long originalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    private OrderShipStatus orderShipStatus;

    @Column
    private Long memberId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    public void addOrderDetail(OrderDetail orderDetail) {
        this.orderDetails.add(orderDetail);
        orderDetail.setOrder(this);
    }

    public static Order from(final OrderInfo orderInfo) {
        return Order.builder()
                    .id(orderInfo.getId())
                    .originalPrice(orderInfo.getOriginalPrice())
                    .orderStatus(orderInfo.getOrderStatus())
                    .orderShipStatus(orderInfo.getOrderShipStatus())
                    .orderDetails(orderInfo.getOrderDetails()
                                          .stream()
                                          .map(OrderDetail::of)
                                          .toList())
                    .build();
    }

    public OrderInfo toInfo() {
        return OrderInfo.builder()
                        .id(id)
                        .originalPrice(originalPrice)
                        .orderStatus(orderStatus)
                        .orderShipStatus(orderShipStatus)
                        .orderStatus(orderStatus)
                        .orderDetails(orderDetails.stream()
                                                  .map(OrderDetail::toInfo)
                                                  .toList())
                        .build();
    }
}
