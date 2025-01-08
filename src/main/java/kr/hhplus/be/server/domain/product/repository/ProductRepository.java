package kr.hhplus.be.server.domain.product.repository;

import kr.hhplus.be.server.domain.product.info.ProductInfo;
import kr.hhplus.be.server.domain.product.info.ProductInventoryInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository {
    Page<ProductInfo> findAllProducts(Pageable pageable);
    ProductInventoryInfo findProductWithDetailById(Long productId);
}
