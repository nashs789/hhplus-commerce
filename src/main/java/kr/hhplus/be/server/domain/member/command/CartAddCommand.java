package kr.hhplus.be.server.domain.member.command;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartAddCommand {
    private Long productId;
    private Long cnt;
}
