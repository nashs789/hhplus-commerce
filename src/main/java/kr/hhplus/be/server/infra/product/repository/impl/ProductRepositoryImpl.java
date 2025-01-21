package kr.hhplus.be.server.infra.product.repository.impl;

import kr.hhplus.be.server.domain.product.exception.ProductException;
import kr.hhplus.be.server.domain.product.info.ProductInfo;
import kr.hhplus.be.server.domain.product.info.ProductInventoryInfo;
import kr.hhplus.be.server.domain.product.repository.ProductRepository;
import kr.hhplus.be.server.infra.product.entity.Product;
import kr.hhplus.be.server.infra.product.entity.ProductInventory;
import kr.hhplus.be.server.infra.product.repository.ProductInventoryJpaRepository;
import kr.hhplus.be.server.infra.product.repository.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.hhplus.be.server.domain.product.exception.ProductException.ProductExceptionCode.NO_SUCH_PRODUCT;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;
    private final ProductInventoryJpaRepository productInventoryJpaRepository;

    @Override
    public List<ProductInfo> findProductsByIds(final List<Long> productIds) {
        return productJpaRepository.findAllByIdIn(productIds)
                                   .orElseThrow(() -> new ProductException(NO_SUCH_PRODUCT))
                                   .stream()
                                   .map(Product::toInfo)
                                   .toList();
    }

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

    @Override
    public ProductInventoryInfo findByProductIdWithLock(final Long productId) {
        return productInventoryJpaRepository.findByProductIdWithLock(productId)
                                            .orElseThrow(() -> new ProductException(NO_SUCH_PRODUCT))
                                            .toInfo();
    }

    @Override
    public void reduceProductStock(final ProductInventoryInfo productInventoryInfo) {
        ProductInventory productInventory = ProductInventory.create(productInventoryInfo);

        productInventoryJpaRepository.save(productInventory);
    }
}
