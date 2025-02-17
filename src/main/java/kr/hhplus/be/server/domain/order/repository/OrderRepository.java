package kr.hhplus.be.server.domain.order.repository;

import kr.hhplus.be.server.domain.member.info.CartProductInfo;
import kr.hhplus.be.server.domain.order.info.OrderInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository {

    OrderInfo findOrderById(Long orderId);
    void changeOrderStatus(OrderInfo orderInfo);
    OrderInfo createOrder(Long memberId, Long orderPrice, List<CartProductInfo> productsInCart);
}
