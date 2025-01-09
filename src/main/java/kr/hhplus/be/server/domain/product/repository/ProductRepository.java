package kr.hhplus.be.server.domain.product.repository;

import kr.hhplus.be.server.domain.product.info.ProductInfo;
import kr.hhplus.be.server.domain.product.info.ProductInventoryInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository {
    List<ProductInfo> findProductsByIds(List<Long> ids);
    Page<ProductInfo> findAllProducts(Pageable pageable);
    ProductInventoryInfo findProductWithDetailById(Long productId);
}
