package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.info.CouponHistoryInfo;
import kr.hhplus.be.server.domain.coupon.info.CouponInfo;
import kr.hhplus.be.server.domain.coupon.service.CouponService;
import kr.hhplus.be.server.domain.member.info.MemberInfo;
import kr.hhplus.be.server.domain.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static kr.hhplus.be.server.infra.coupon.entity.CouponHistory.CouponStatus.NOT_USED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CouponFacadeTest {

    @Mock
    private MemberService memberService;

    @Mock
    private CouponService couponService;

    @InjectMocks
    private CouponFacade couponFacade;

    @Test
    @DisplayName("쿠폰 정상 발급")
    void successPublishCoupon() {
        // given
        MemberInfo memberInfo = MemberInfo.builder()
                                          .build();
        CouponInfo couponInfo = CouponInfo.builder()
                                          .publishedQuantity(0)
                                          .totalQuantity(10)
                                          .expiredAt(LocalDateTime.now().plusDays(1))
                                          .build();
        CouponHistoryInfo couponHistoryInfo = CouponHistoryInfo.builder()
                                                               .memberInfo(memberInfo)
                                                               .couponInfo(couponInfo)
                                                               .status(NOT_USED)
                                                               .build();
        when(memberService.findMemberById(anyLong())).thenReturn(memberInfo);
        when(couponService.findCouponByIdWithLock(anyLong())).thenReturn(couponInfo);
        when(couponFacade.applyCouponById(1L, 1L)).thenReturn(couponHistoryInfo);

        // when
        CouponHistoryInfo result = couponFacade.applyCouponById(1L, 1L);

        // then
        assertEquals(memberInfo, result.getMemberInfo());
        assertEquals(couponInfo, result.getCouponInfo());
    }
}