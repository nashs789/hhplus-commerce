package kr.hhplus.be.server.api.product.response;

public record ProductResponse(
        Long id,
        String name,
        Long price,
        String image
) {
}
