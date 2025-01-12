package kr.hhplus.be.server.domain.product.info;

import kr.hhplus.be.server.api.product.response.ProductResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductInfo {
    private Long id;
    private String name;
    private Long price;
    private String image;

    public ProductResponse toResponse() {
        return new ProductResponse(
                id, name, price, image
        );
    }
}
