package kr.hhplus.be.server.domain.product.service;

import kr.hhplus.be.server.domain.product.info.ProductInfo;
import kr.hhplus.be.server.domain.product.info.ProductInventoryInfo;
import kr.hhplus.be.server.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    /** 상품 조회 */
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<ProductInfo> findProductsByIds(final List<Long> productIds) {
        return productRepository.findProductsByIds(productIds);
    }

    @Transactional(readOnly = true)
    public Page<ProductInfo> findAllProducts(final int page, final int size) {
        Pageable pageable = PageRequest.of(page, size);

        return productRepository.findAllProducts(pageable);
    }

    @Transactional
    public ProductInventoryInfo findProductWithDetailById(final Long productId) {
        return productRepository.findProductWithDetailById(productId);
    }
}
