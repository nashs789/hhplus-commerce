package kr.hhplus.be.server.application.payment.facade;

import kr.hhplus.be.server.application.payment.external.PaymentSystem;
import kr.hhplus.be.server.domain.coupon.info.CouponHistoryInfo;
import kr.hhplus.be.server.domain.coupon.service.CouponService;
import kr.hhplus.be.server.domain.member.command.CartDeleteCommand;
import kr.hhplus.be.server.domain.member.command.PointUseCommand;
import kr.hhplus.be.server.domain.member.info.MemberInfo;
import kr.hhplus.be.server.domain.member.service.MemberService;
import kr.hhplus.be.server.domain.order.info.OrderDetailInfo;
import kr.hhplus.be.server.domain.order.info.OrderInfo;
import kr.hhplus.be.server.domain.order.service.OrderService;
import kr.hhplus.be.server.domain.payment.info.PaymentInfo;
import kr.hhplus.be.server.domain.payment.service.PaymentService;
import kr.hhplus.be.server.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static kr.hhplus.be.server.infra.member.entity.PointHistory.PointUseType.USE;

@Component
@RequiredArgsConstructor
public class PaymentFacade {

    private final MemberService memberService;
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final PaymentSystem paymentSystem;
    private final CouponService couponService;
    private final ProductService productService;

    @Transactional
    public PaymentInfo paymentProgress(final Long memberId, final Long orderId, final Long couponId) {
        MemberInfo memberInfo = memberService.findByMemberIdWithLock(memberId);
        OrderInfo orderInfo = orderService.findOrderById(orderId);

        // 쿠폰을 선택 했다면 쿠폰 가격 적용
        if(!Objects.isNull(couponId)) {
            CouponHistoryInfo couponHistoryInfo = couponService.findCouponHistoryByIdWithLock(couponId, memberId);

            couponHistoryInfo.useCoupon();
            couponService.changeCouponHistoryStatus(couponHistoryInfo, memberId);
            orderInfo.applyCoupon(couponHistoryInfo.getCouponInfo());
        }

        // 잔고 확인
        memberInfo.isAffordable(orderInfo.getFinalPrice());

        // 결제 시스템 호출
        boolean isPaymentOk = paymentSystem.sendPayment(memberId, orderInfo.getFinalPrice());

        // 결제가 정상 -> 포인트 차감
        if(isPaymentOk) {
            List<CartDeleteCommand> deleteProductList = new ArrayList<>();

            memberService.useMemberPoint(PointUseCommand.builder()
                                                        .memberId(memberId)
                                                        .usePoint(orderInfo.getFinalPrice())
                                                        .pointUseType(USE)
                                                        .build());

            orderInfo.orderPayDone();
            orderService.changeOrderStatus(orderInfo);

            for(OrderDetailInfo orderDetail : orderInfo.getOrderDetails()) {
                Long quantity = orderDetail.getQuantity();
                Long productId = orderDetail.getProductInfo()
                                            .getId();

                productService.reduceProductStock(productId, quantity);
                deleteProductList.add(CartDeleteCommand.builder()
                                                       .cartProductId(productId)
                                                       .build());
            }

            memberService.deleteCartByProductId(deleteProductList);
        }

        PaymentInfo paymentInfo = paymentService.savePaymentResult(isPaymentOk, orderInfo);

        paymentInfo.setOrderInfo(orderInfo);

        return paymentInfo;
    }
}
