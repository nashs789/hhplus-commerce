package kr.hhplus.be.server.api.order.request;

import kr.hhplus.be.server.api.product.request.ProductAddRequest;

import java.util.List;

public record OrderRequest(
        Long userId,
        List<ProductAddRequest> products
) {
}
