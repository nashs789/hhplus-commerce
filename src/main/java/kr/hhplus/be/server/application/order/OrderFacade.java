package kr.hhplus.be.server.application.order;

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
        // TODO - 쿠폰 할인가 적용하는 코드도 있어야 할 것 같은데
        List<CartProductInfo> productsInCart = memberService.findCartProductsByMemberIdWithLock(memberId);

        // TODO - 카트 품목 삭제
        return orderService.createOrder(productsInCart, memberId);
    }
}
