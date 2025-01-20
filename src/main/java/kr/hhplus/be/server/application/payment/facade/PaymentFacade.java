package kr.hhplus.be.server.application.payment.facade;

import kr.hhplus.be.server.application.payment.external.PaymentSystem;
import kr.hhplus.be.server.domain.coupon.info.CouponHistoryInfo;
import kr.hhplus.be.server.domain.coupon.service.CouponService;
import kr.hhplus.be.server.domain.member.command.PointUseCommand;
import kr.hhplus.be.server.domain.member.info.MemberInfo;
import kr.hhplus.be.server.domain.member.service.MemberService;
import kr.hhplus.be.server.domain.order.info.OrderInfo;
import kr.hhplus.be.server.domain.order.service.OrderService;
import kr.hhplus.be.server.domain.payment.exception.PaymentException;
import kr.hhplus.be.server.domain.payment.info.PaymentInfo;
import kr.hhplus.be.server.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static kr.hhplus.be.server.domain.payment.exception.PaymentException.PaymentExceptionCode.FAIL_PAYMENT;
import static kr.hhplus.be.server.infra.coupon.entity.CouponHistory.CouponStatus.USED;
import static kr.hhplus.be.server.infra.member.entity.PointHistory.PointUseType.USE;
import static kr.hhplus.be.server.infra.order.entity.Order.OrderStatus.PAYED;

@Component
@RequiredArgsConstructor
public class PaymentFacade {

    private final MemberService memberService;
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final PaymentSystem paymentSystem;
    private final CouponService couponService;

    @Transactional
    public PaymentInfo paymentProgress(final Long memberId, final Long orderId, final Long couponId) {
        MemberInfo memberInfo = memberService.findByMemberIdWithLock(memberId);
        OrderInfo orderInfo = orderService.findOrderById(orderId);

        // 쿠폰을 선택 했다면 쿠폰 가격 적용
        if(!Objects.isNull(couponId)) {
            CouponHistoryInfo couponHistoryInfo = couponService.findCouponHistoryByIdWithLock(couponId, memberId);

            couponHistoryInfo.setStatus(USED);
            couponService.changeCouponHistoryStatus(couponHistoryInfo, memberId);
            orderInfo.applyCoupon(couponHistoryInfo.getCouponInfo());
        }

        // 잔고 확인
        memberInfo.isAffordable(orderInfo.getFinalPrice());

        // 결제 시스템 호출
        boolean isPaymentOk = paymentSystem.sendPayment(memberId, orderInfo.getFinalPrice());

        // 결제가 정상 -> 포인트 차감
        if(isPaymentOk) {
            memberService.useMemberPoint(PointUseCommand.builder()
                                                        .memberId(memberId)
                                                        .usePoint(orderInfo.getFinalPrice())
                                                        .pointUseType(USE)
                                                        .build());
        }

        PaymentInfo paymentInfo = paymentService.savePaymentResult(isPaymentOk, orderInfo);

        orderInfo.setOrderStatus(PAYED);
        orderService.changeOrderStatus(orderInfo);

        // TODO - 재고 차감
        // TODO - 장바구니 clear

        paymentInfo.setOrderInfo(orderInfo);
        return paymentInfo;
    }
}
