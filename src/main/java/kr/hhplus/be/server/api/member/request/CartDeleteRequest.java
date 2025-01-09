package kr.hhplus.be.server.api.member.request;

import kr.hhplus.be.server.api.product.request.ProductDeleteRequest;

import java.util.List;

public record CartDeleteRequest(
        List<ProductDeleteRequest> products
) {
}
