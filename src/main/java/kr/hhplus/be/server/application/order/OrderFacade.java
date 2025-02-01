package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.member.command.CartDeleteCommand;
import kr.hhplus.be.server.domain.member.info.CartProductInfo;
import kr.hhplus.be.server.domain.member.service.MemberService;
import kr.hhplus.be.server.domain.order.info.OrderInfo;
import kr.hhplus.be.server.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderFacade {

    private final MemberService memberService;
    private final OrderService orderService;

    @Transactional
    public OrderInfo createOrder(final Long memberId) {
        List<CartProductInfo> productsInCart = memberService.findCartProductsByMemberIdWithLock(memberId);

        memberService.deleteCartByProductId(productsInCart.stream()
                                                          .map(e -> CartDeleteCommand.builder()
                                                                                     .cartProductId(e.getProductInfo().getId())
                                                                                     .build()
                                                          )
                                                          .toList());

        return orderService.createOrder(productsInCart, memberId);
    }
}
