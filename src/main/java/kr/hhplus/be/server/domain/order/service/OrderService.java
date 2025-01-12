package kr.hhplus.be.server.domain.order.service;

import kr.hhplus.be.server.domain.member.info.CartProductInfo;
import kr.hhplus.be.server.domain.order.info.OrderInfo;
import kr.hhplus.be.server.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public OrderInfo createOrder(
            final List<CartProductInfo> productsInCart,
            final Long memberId
    ) {
        final Long finalPrice = productsInCart.stream()
                                              .mapToLong(e -> e.getProductInfo().getPrice() * e.getQuantity())
                                              .sum();

        return orderRepository.createOrder(memberId, finalPrice, productsInCart);
    }
}
