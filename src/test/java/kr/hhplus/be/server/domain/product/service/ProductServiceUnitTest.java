package kr.hhplus.be.server.domain.product.service;

import kr.hhplus.be.server.domain.product.info.ProductInfo;
import kr.hhplus.be.server.domain.product.info.ProductInventoryInfo;
import kr.hhplus.be.server.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceUnitTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    @Test
    @DisplayName("상품 리스트 조회")
    void findAllProducts() {
        // given
        List<ProductInfo> productInfos = List.of(
                ProductInfo.builder().build(),
                ProductInfo.builder().build(),
                ProductInfo.builder().build()
        );
        Page<ProductInfo> productInfoPage = new PageImpl<>(productInfos, PageRequest.of(0, 10), productInfos.size());
        when(productRepository.findAllProducts(any())).thenReturn(productInfoPage);

        // when
        Page<ProductInfo> allProducts = productService.findAllProducts(0, 10);

        // then
        assertEquals(productInfos.size(), allProducts.get().count());
        assertThat(productInfoPage.get().toList()).containsExactlyInAnyOrderElementsOf(allProducts);
    }

    @Test
    @DisplayName("상품 상세 정보 조회")
    void findProductWithDetail() {
        // given
        ProductInfo productInfo = ProductInfo.builder().build();
        ProductInventoryInfo productInventoryInfo = ProductInventoryInfo.builder()
                                                                        .productInfo(productInfo)
                                                                        .build();
        when(productRepository.findProductWithDetailById(anyLong())).thenReturn(productInventoryInfo);

        // when
        ProductInventoryInfo productWithDetailById = productService.findProductWithDetailById(1L);

        // then
        assertEquals(productWithDetailById.getProductInfo(), productInfo);
    }
}