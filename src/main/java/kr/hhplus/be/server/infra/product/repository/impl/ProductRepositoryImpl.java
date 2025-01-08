package kr.hhplus.be.server.infra.product.repository.impl;

import kr.hhplus.be.server.domain.product.exception.ProductException;
import kr.hhplus.be.server.domain.product.info.ProductInfo;
import kr.hhplus.be.server.domain.product.info.ProductInventoryInfo;
import kr.hhplus.be.server.domain.product.repository.ProductRepository;
import kr.hhplus.be.server.infra.product.entity.Product;
import kr.hhplus.be.server.infra.product.repository.ProductInventoryJpaRepository;
import kr.hhplus.be.server.infra.product.repository.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static kr.hhplus.be.server.domain.product.exception.ProductException.ProductExceptionCode.NO_SUCH_PRODUCT;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    /** 상품 정보 */
    private final ProductJpaRepository productJpaRepository;
    /** 상품 상세 정보 */
    private final ProductInventoryJpaRepository productInventoryJpaRepository;

    @Override
    public Page<ProductInfo> findAllProducts(final Pageable pageable) {
        return productJpaRepository.findAllProducts(pageable)
                                   .map(Product::toInfo);
    }

    @Override
    public ProductInventoryInfo findProductWithDetailById(final Long productId) {
        return productInventoryJpaRepository.findByProductId(productId)
                                            .orElseThrow(() -> new ProductException(NO_SUCH_PRODUCT))
                                            .toInfo();
    }
}
