package kr.hhplus.be.server.api.product.response;

public record ProductInventoryResponse(
        Long id,
        Long stock,
        ProductResponse productResponse
) {
}
