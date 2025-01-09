package kr.hhplus.be.server.domain.member.info;

import kr.hhplus.be.server.api.member.response.CartResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartInfo {
    private Long id;
    private MemberInfo memberInfo;

    public CartResponse toResponse() {
        return new CartResponse();
    }
}
