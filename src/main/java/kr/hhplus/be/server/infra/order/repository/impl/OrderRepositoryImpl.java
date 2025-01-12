package kr.hhplus.be.server.infra.order.repository.impl;

import kr.hhplus.be.server.domain.member.info.CartProductInfo;
import kr.hhplus.be.server.domain.order.info.OrderInfo;
import kr.hhplus.be.server.domain.order.repository.OrderRepository;
import kr.hhplus.be.server.infra.member.entity.Member;
import kr.hhplus.be.server.infra.order.entity.Order;
import kr.hhplus.be.server.infra.order.entity.OrderDetail;
import kr.hhplus.be.server.infra.order.repository.OrderJpaRepository;
import kr.hhplus.be.server.infra.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static kr.hhplus.be.server.infra.order.entity.Order.OrderShipStatus.NOT_DEPARTURE;
import static kr.hhplus.be.server.infra.order.entity.Order.OrderStatus.NOT_PAYED;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public OrderInfo createOrder(final Long memberId, final Long finalPrice, final List<CartProductInfo> productsInCart) {
        Order order = Order.builder()
                           .orderStatus(NOT_PAYED)
                           .orderShipStatus(NOT_DEPARTURE)
                           .finalPrice(finalPrice)
                           .originalPrice(finalPrice)
                           .orderDetails(new ArrayList<>())
                           .member(Member.builder()
                                         .id(memberId)
                                         .build())
                           .build();
        List<OrderDetail> orderDetails = productsInCart.stream()
                                                       .map(productInfo -> OrderDetail.builder()
                                                                                    .product(Product.builder()
                                                                                                    .id(productInfo.getProductInfo().getId())
                                                                                                    .price(productInfo.getProductInfo().getPrice())
                                                                                                    .name(productInfo.getProductInfo().getName())
                                                                                                    .image(productInfo.getProductInfo().getImage())
                                                                                                    .build())
                                                                                    .order(order)
                                                                                    .quantity(productInfo.getQuantity())
                                                                                    .build()
                                                       )
                                                       .collect(Collectors.toUnmodifiableList());

        orderDetails.forEach(order::addOrderDetail);

        return orderJpaRepository.save(order)
                                 .toInfo();
    }
}
