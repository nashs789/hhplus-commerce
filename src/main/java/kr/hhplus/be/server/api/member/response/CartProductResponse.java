package kr.hhplus.be.server.api.member.response;

import kr.hhplus.be.server.api.product.response.ProductResponse;

public record CartProductResponse(
        Long id,
        ProductResponse productResponse,
        Long quantity,
        CartResponse cartResponse
) {
}
