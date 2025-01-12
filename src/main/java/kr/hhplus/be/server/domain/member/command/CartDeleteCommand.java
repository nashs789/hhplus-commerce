package kr.hhplus.be.server.domain.member.command;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartDeleteCommand {
    private Long cartProductId;
}
