package kr.hhplus.be.server.api.product.controller;

import kr.hhplus.be.server.api.product.response.ProductInventoryResponse;
import kr.hhplus.be.server.api.product.response.ProductResponse;
import kr.hhplus.be.server.domain.product.info.ProductInfo;
import kr.hhplus.be.server.domain.product.info.ProductInventoryInfo;
import kr.hhplus.be.server.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController implements ProductApi {

    private final ProductService productService;

    @Override
    public ResponseEntity<Page<ProductResponse>> findAllProducts(
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "3") final int size
    ) {
        return ResponseEntity.ok(productService.findAllProducts(page, size)
                                               .map(ProductInfo::toResponse));
    }

    @Override
    public ResponseEntity<ProductInventoryResponse> findProductWithDetailById(
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(productService.findProductWithDetailById(productId)
                                               .toResponse());
    }

    @Override
    public ResponseEntity<List<ProductResponse>> findProductRanking() {
        return ResponseEntity.ok(
                List.of(

                )
        );
    }
}
