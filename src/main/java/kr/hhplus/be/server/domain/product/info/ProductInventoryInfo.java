package kr.hhplus.be.server.domain.product.info;

import kr.hhplus.be.server.api.product.response.ProductInventoryResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductInventoryInfo {
    Long id;
    Long stock;
    ProductInfo productInfo;

    public ProductInventoryResponse toResponse() {
        return new ProductInventoryResponse(
                id, stock, productInfo.toResponse()
        );
    }
}
