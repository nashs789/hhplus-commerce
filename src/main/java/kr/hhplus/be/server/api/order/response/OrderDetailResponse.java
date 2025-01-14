package kr.hhplus.be.server.api.order.response;

import kr.hhplus.be.server.api.product.response.ProductResponse;

public record OrderDetailResponse(
        ProductResponse productResponse
) {
}
