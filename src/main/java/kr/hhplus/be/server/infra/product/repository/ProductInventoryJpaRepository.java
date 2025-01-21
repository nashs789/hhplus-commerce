package kr.hhplus.be.server.infra.product.repository;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.infra.product.entity.ProductInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductInventoryJpaRepository extends JpaRepository<ProductInventory, Long> {

    @Query("""
        SELECT pi
          FROM ProductInventory pi
          JOIN Product p
            ON pi.product = p
         WHERE p.id = :productId
    """)
    Optional<ProductInventory> findByProductId(@Param("productId") final Long productId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT pi
          FROM ProductInventory pi
         WHERE pi.id = :productInventoryId
    """)
    Optional<ProductInventory> findByProductInventoryWithLock(@Param("productInventoryId") final Long productInventoryId);

    Optional<List<ProductInventory>> findByIdIn(List<Long> productIds);
}
