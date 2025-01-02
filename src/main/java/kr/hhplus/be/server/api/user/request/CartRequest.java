package kr.hhplus.be.server.api.user.request;

import kr.hhplus.be.server.api.product.request.ProductRequest;

import java.util.List;

public record CartRequest(
        List<ProductRequest> productRequests
) {
}
