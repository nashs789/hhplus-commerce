package kr.hhplus.be.server.application.payment.facade;

import kr.hhplus.be.server.domain.member.command.PointUseCommand;
import kr.hhplus.be.server.domain.member.info.MemberInfo;
import kr.hhplus.be.server.domain.member.service.MemberService;
import kr.hhplus.be.server.domain.order.info.OrderInfo;
import kr.hhplus.be.server.domain.order.service.OrderService;
import kr.hhplus.be.server.application.payment.external.PaymentSystem;
import kr.hhplus.be.server.domain.payment.info.PaymentInfo;
import kr.hhplus.be.server.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static kr.hhplus.be.server.infra.member.entity.PointHistory.PointUseType.USE;

@Component
@RequiredArgsConstructor
public class PaymentFacade {

    private final MemberService memberService;
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final PaymentSystem paymentSystem;

    @Transactional
    public PaymentInfo paymentProgress(final Long memberId, final Long orderId) {
        MemberInfo memberInfo = memberService.findByMemberIdWithLock(memberId);
        OrderInfo orderInfo = orderService.findOrderById(orderId);

        // TODO - 쿠폰 적용

        memberInfo.isAffordable(orderInfo.getFinalPrice());
        boolean paymentResult = paymentSystem.sendPayment(memberId, orderInfo.getFinalPrice());
        PaymentInfo paymentInfo = paymentService.savePaymentResult(paymentResult, orderInfo);

        if(Objects.nonNull(paymentInfo)) {
            memberService.useMemberPoint(PointUseCommand.builder()
                                                        .memberId(memberId)
                                                        .usePoint(orderInfo.getFinalPrice())
                                                        .pointUseType(USE)
                                                        .build());
        }

        // TODO - 오더 상태 변경

        return paymentInfo;
    }
}
