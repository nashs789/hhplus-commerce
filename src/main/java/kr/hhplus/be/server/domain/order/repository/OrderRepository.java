package kr.hhplus.be.server.domain.order.repository;

import kr.hhplus.be.server.domain.member.info.CartProductInfo;
import kr.hhplus.be.server.domain.order.info.OrderInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository {

    OrderInfo createOrder(Long memberId, Long finalPrice, List<CartProductInfo> productsInCart);
}
