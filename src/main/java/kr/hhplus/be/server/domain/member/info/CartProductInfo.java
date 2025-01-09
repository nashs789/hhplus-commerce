package kr.hhplus.be.server.domain.member.info;

import kr.hhplus.be.server.api.member.response.CartProductResponse;
import kr.hhplus.be.server.domain.product.info.ProductInfo;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartProductInfo {
    private Long id;
    private ProductInfo productInfo;
    private Long cnt;
    private CartInfo cartInfo;

    public CartProductResponse toResponse() {
        return new CartProductResponse(
                id,
                productInfo.toResponse(),
                cnt,
                cartInfo.toResponse()
        );
    }
}
