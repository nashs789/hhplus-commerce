package kr.hhplus.be.server.domain.order.info;

import kr.hhplus.be.server.api.order.response.OrderDetailResponse;
import kr.hhplus.be.server.domain.product.info.ProductInfo;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDetailInfo {
    private Long id;
    private OrderInfo orderInfo;
    private ProductInfo productInfo;
    private Long quantity;

    public OrderDetailResponse toResponse() {
        return new OrderDetailResponse(
                productInfo.toResponse()
        );
    }
}
