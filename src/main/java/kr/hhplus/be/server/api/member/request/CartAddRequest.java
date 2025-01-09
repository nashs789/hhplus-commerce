package kr.hhplus.be.server.api.member.request;

import kr.hhplus.be.server.api.product.request.ProductAddRequest;

import java.util.List;

public record CartAddRequest(
        List<ProductAddRequest> products
) {

}
