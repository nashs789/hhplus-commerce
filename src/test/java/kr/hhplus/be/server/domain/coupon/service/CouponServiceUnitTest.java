package kr.hhplus.be.server.domain.coupon.service;

import kr.hhplus.be.server.domain.coupon.exception.CouponException;
import kr.hhplus.be.server.domain.coupon.info.CouponHistoryInfo;
import kr.hhplus.be.server.domain.coupon.info.CouponInfo;
import kr.hhplus.be.server.domain.coupon.repository.CouponRepository;
import kr.hhplus.be.server.domain.member.info.MemberInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

import static kr.hhplus.be.server.domain.coupon.exception.CouponException.CouponExceptionCode.NOT_EXIST_COUPON;
import static kr.hhplus.be.server.domain.coupon.exception.CouponException.CouponExceptionCode.NOT_EXIST_COUPON_HISTORY;
import static kr.hhplus.be.server.infra.coupon.entity.CouponHistory.CouponStatus.NOT_USED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CouponServiceUnitTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponService couponService;

    @Test
    @DisplayName("쿠폰 조회")
    void findCouponById() {
        // given
        CouponInfo couponInfo = CouponInfo.builder()
                                          .code("Coupon A")
                                          .build();
        when(couponRepository.findCouponById(anyLong())).thenReturn(couponInfo);

        // when
        CouponInfo result = couponService.findCouponById(1L);

        // then
        assertEquals(couponInfo, result);
        assertEquals(couponInfo.getCode(), result.getCode());
    }

    @Test
    @DisplayName("쿠폰 조회 실패")
    void findCouponByNotExistId() {
        // given
        when(couponRepository.findCouponById(anyLong())).thenThrow(new CouponException(NOT_EXIST_COUPON));

        // when
        CouponException couponException = assertThrows(CouponException.class, () ->
                couponService.findCouponById(1L)
        );

        // then
        assertEquals(HttpStatus.BAD_REQUEST, couponException.getStatus());
        assertEquals("존재하지 않는 쿠폰 입니다.", couponException.getMessage());
    }

    @Test
    @DisplayName("쿠폰 발급 이력 조회(보유한 쿠폰 조회)")
    void findCouponHistoryById() {
        // given
        List<CouponHistoryInfo> couponHistoryInfoList = List.of(
                CouponHistoryInfo.builder().build(),
                CouponHistoryInfo.builder().build(),
                CouponHistoryInfo.builder().build()
        );
        when(couponRepository.findCouponHistoryMemberById(anyLong())).thenReturn(couponHistoryInfoList);

        // when
        List<CouponHistoryInfo> result = couponService.findCouponHistoryMemberById(1L);

        // then
        assertEquals(couponHistoryInfoList.size(), result.size());
        assertThat(result).containsExactlyInAnyOrderElementsOf(couponHistoryInfoList);
    }

    @Test
    @DisplayName("쿠폰 이력 없음")
    void findNoCouponHistory() {
        // given
        when(couponRepository.findCouponHistoryMemberById(anyLong())).thenThrow(new CouponException(NOT_EXIST_COUPON_HISTORY));

        // when
        CouponException couponException = assertThrows(CouponException.class, () ->
                couponService.findCouponHistoryMemberById(1L)
        );

        // then
        assertEquals(HttpStatus.BAD_REQUEST, couponException.getStatus());
        assertEquals("쿠폰 발급 정보가 없습니다.", couponException.getMessage());
    }

    @Test
    @DisplayName("쿠폰 발급")
    void applyPublishedCoupon() {
        // given
        CouponInfo couponInfo = CouponInfo.builder()
                                          .id(1L)
                                          .publishedQuantity(0)
                                          .totalQuantity(10)
                                          .expiredAt(LocalDateTime.now().plusDays(1))
                                          .build();
        MemberInfo memberInfo = MemberInfo.builder().build();
        CouponHistoryInfo couponHistoryInfo = CouponHistoryInfo.builder()
                                                               .status(NOT_USED)
                                                               .build();
        when(couponRepository.applyPublishedCoupon(couponInfo, memberInfo.getId())).thenReturn(couponHistoryInfo);
        when(couponRepository.findCouponById(anyLong())).thenReturn(couponInfo);
        // when
        CouponHistoryInfo result = couponService.applyPublishedCoupon(couponInfo.getId(), memberInfo);

        // then
        assertEquals(couponHistoryInfo, result);
    }
}