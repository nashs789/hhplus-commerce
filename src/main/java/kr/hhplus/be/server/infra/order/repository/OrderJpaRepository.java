package kr.hhplus.be.server.infra.order.repository;

import kr.hhplus.be.server.infra.order.entity.Order;
import kr.hhplus.be.server.infra.order.entity.Order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {

    @Modifying
    @Query("""
        UPDATE Order o
          SET o.orderStatus = :orderStatus
         WHERE o.id = :orderId
    """)
    void changeOrderStatus(
            @Param("orderId") final Long orderId,
            @Param("orderStatus") final OrderStatus orderStatus
    );
}
