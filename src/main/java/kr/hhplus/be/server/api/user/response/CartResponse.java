package kr.hhplus.be.server.api.user.response;

import kr.hhplus.be.server.api.product.response.ProductResponse;

import java.util.List;

public record CartResponse(
        List<ProductResponse> products
) {
}
