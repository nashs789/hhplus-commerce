package kr.hhplus.be.server.infra.order.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.order.info.OrderInfo;
import kr.hhplus.be.server.infra.common.entity.Timestamp;
import kr.hhplus.be.server.infra.member.entity.Member;
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
    private Long finalPrice;

    @Column
    private Long originalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    private OrderShipStatus orderShipStatus;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    public void addOrderDetail(OrderDetail orderDetail) {
        this.orderDetails.add(orderDetail);
        orderDetail.setOrder(this);
    }

    public OrderInfo toInfo() {
        return OrderInfo.builder()
                        .id(id)
                        .finalPrice(finalPrice)
                        .originalPrice(finalPrice)
                        .orderStatus(orderStatus)
                        .orderShipStatus(orderShipStatus)
                        .orderStatus(orderStatus)
                        .orderDetails(orderDetails.stream()
                                                  .map(OrderDetail::toInfo)
                                                  .toList())
                        .build();
    }
}
