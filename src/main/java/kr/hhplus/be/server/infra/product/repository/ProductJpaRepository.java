package kr.hhplus.be.server.infra.product.repository;

import kr.hhplus.be.server.infra.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {

    Optional<List<Product>> findAllByIdIn(List<Long> ids);

    @Query(value = """
        SELECT p
          FROM Product p
    """,
    countQuery = """
        SELECT COUNT(p)
          FROM Product p
    """)
    Page<Product> findAllProducts(Pageable pageable);
}
