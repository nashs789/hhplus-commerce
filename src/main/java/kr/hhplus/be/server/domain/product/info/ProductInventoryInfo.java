package kr.hhplus.be.server.domain.product.info;

import kr.hhplus.be.server.api.product.response.ProductInventoryResponse;
import kr.hhplus.be.server.domain.product.exception.ProductException;
import lombok.Builder;
import lombok.Data;

import static kr.hhplus.be.server.domain.product.exception.ProductException.ProductExceptionCode.INSUFFICIENT_STOCK;

@Data
@Builder
public class ProductInventoryInfo {
    Long id;
    Long stock;
    ProductInfo productInfo;

    public void checkStock(final Long quantity) {
        if(stock.compareTo(quantity) < 0) {
            throw new ProductException(INSUFFICIENT_STOCK);
        }

        stock -= quantity;
    }

    public ProductInventoryResponse toResponse() {
        return new ProductInventoryResponse(
                id, stock, productInfo.toResponse()
        );
    }
}
