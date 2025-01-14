package kr.hhplus.be.server.domain.member.service;

import kr.hhplus.be.server.domain.member.command.CartAddCommand;
import kr.hhplus.be.server.domain.member.command.PointChargeCommand;
import kr.hhplus.be.server.domain.member.command.PointUseCommand;
import kr.hhplus.be.server.domain.member.exception.CartException;
import kr.hhplus.be.server.domain.member.exception.MemberException;
import kr.hhplus.be.server.domain.member.exception.PointException;
import kr.hhplus.be.server.domain.member.info.CartInfo;
import kr.hhplus.be.server.domain.member.info.CartProductInfo;
import kr.hhplus.be.server.domain.member.info.MemberInfo;
import kr.hhplus.be.server.domain.member.info.PointHistoryInfo;
import kr.hhplus.be.server.domain.member.repository.MemberRepository;
import kr.hhplus.be.server.domain.product.info.ProductInfo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static kr.hhplus.be.server.domain.member.exception.CartException.CartExceptionCode.NO_SUCH_CART;
import static kr.hhplus.be.server.domain.member.exception.MemberException.MemberExceptionCode.NO_SUCH_MEMBER;
import static kr.hhplus.be.server.domain.member.exception.PointException.PointExceptionCode.FAIL_CHARGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceUnitTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("유저 포인트 조회")
    void findMemberPointById() {
        // given
        final Long MEMBER_ID = 1L;
        final Long POINT = 10_000L;
        final MemberInfo returnObj = MemberInfo.builder()
                                               .id(MEMBER_ID)
                                               .point(POINT)
                                               .build();
        when(memberRepository.findMemberById(MEMBER_ID)).thenReturn(returnObj);

        // when
        MemberInfo memberById = memberService.findMemberById(MEMBER_ID);

        // then
        assertEquals(returnObj, memberById);
        assertEquals(returnObj.getPoint(), POINT);
    }

    @Test
    @DisplayName("존재 하지 않는 유저 아이디 조회")
    void findNotExistMember() {
        // given
        final Long MEMBER_ID = 1L;
        when(memberRepository.findMemberById(MEMBER_ID)).thenThrow(new MemberException(NO_SUCH_MEMBER));

        // when
        MemberException memberException = assertThrows(MemberException.class, () -> {
            memberRepository.findMemberById(MEMBER_ID);
        });
        
        // then
        assertEquals(HttpStatus.BAD_REQUEST, memberException.getStatus());
        assertEquals("존재하지 않는 유저 정보 입니다.", memberException.getMessage());
    }

    @Test
    @DisplayName("유저 포인트 충전 성공")
    void chargeMemberPoint() {
        // given
        final Long MEMBER_ID = 1L;
        final Long BASE_POINT = 10_000L;
        final Long CHARGE_POINT = 20_000L;
        final Long AFTER_POINT = BASE_POINT + CHARGE_POINT;
        PointChargeCommand command = PointChargeCommand.builder()
                                                       .memberId(MEMBER_ID)
                                                       .chargePoint(CHARGE_POINT)
                                                       .build();
        MemberInfo memberInfo = MemberInfo.builder()
                                          .id(MEMBER_ID)
                                          .point(BASE_POINT)
                                          .build();
        PointHistoryInfo history = PointHistoryInfo.builder()
                                                   .beforeAmount(BASE_POINT)
                                                   .afterAmount(AFTER_POINT)
                                                   .build();

        when(memberRepository.findByMemberIdWithLock(MEMBER_ID)).thenReturn(memberInfo);
        when(memberRepository.chargeMemberPoint(memberInfo)).thenReturn(true);
        when(memberRepository.savePointChargeHistory(memberInfo, command)).thenReturn(history);

        // when
        PointHistoryInfo pointHistoryInfo = memberService.chargeMemberPoint(command);

        // then
        assertEquals(BASE_POINT, pointHistoryInfo.getBeforeAmount());
        assertEquals(AFTER_POINT, pointHistoryInfo.getAfterAmount());
    }

    @Test
    @DisplayName("포인트 충전 실패")
    void failChargeMemberPoint() {
        // given
        final Long MEMBER_ID = 1L;
        PointChargeCommand command = PointChargeCommand.builder()
                                                       .memberId(MEMBER_ID)
                                                       .build();
        when(memberRepository.findByMemberIdWithLock(MEMBER_ID)).thenThrow(new PointException(FAIL_CHARGE));

        // when
        PointException pointException = assertThrows(PointException.class, () -> {
           memberService.chargeMemberPoint(command);
        });

        // then
        assertEquals(HttpStatus.BAD_REQUEST, pointException.getStatus());
        assertEquals("포인트 충전에 실패 했습니다.", pointException.getMessage());
    }

    @Test
    @DisplayName("유저 포인트 사용 성공")
    void useMemberPoint() {
        // given
        final Long MEMBER_ID = 1L;
        final Long BASE_POINT = 20_000L;
        final Long USE_POINT = 10_000L;
        final Long AFTER_POINT = BASE_POINT - USE_POINT;
        PointUseCommand command = PointUseCommand.builder()
                                                 .memberId(MEMBER_ID)
                                                 .usePoint(USE_POINT)
                                                 .build();
        MemberInfo memberInfo = MemberInfo.builder()
                                          .id(MEMBER_ID)
                                          .point(BASE_POINT)
                                          .build();
        PointHistoryInfo history = PointHistoryInfo.builder()
                                                   .beforeAmount(BASE_POINT)
                                                   .afterAmount(AFTER_POINT)
                                                   .build();

        when(memberRepository.findByMemberIdWithLock(MEMBER_ID)).thenReturn(memberInfo);
        when(memberRepository.useMemberPoint(memberInfo)).thenReturn(true);
        when(memberRepository.savePointUseHistory(memberInfo, command)).thenReturn(history);

        // when
        PointHistoryInfo pointHistoryInfo = memberService.useMemberPoint(command);

        // then
        assertEquals(BASE_POINT, pointHistoryInfo.getBeforeAmount());
        assertEquals(AFTER_POINT, pointHistoryInfo.getAfterAmount());
    }

    @Test
    @DisplayName("포인트 부족으로 사용 실패")
    void failUseMemberPoint() {
        // given
        final Long MEMBER_ID = 1L;
        final Long BASE_POINT = 10_000L;
        final Long USE_POINT = 20_000L;
        PointUseCommand command = PointUseCommand.builder()
                                                 .memberId(MEMBER_ID)
                                                 .usePoint(USE_POINT)
                                                 .build();
        MemberInfo memberInfo = MemberInfo.builder()
                                          .id(MEMBER_ID)
                                          .point(BASE_POINT)
                                          .build();

        // when
        PointException pointException = assertThrows(PointException.class, () -> memberInfo.usePoint(command.getUsePoint()));

        // then
        assertEquals(HttpStatus.BAD_REQUEST, pointException.getStatus());
        assertEquals("포인트가 부족 합니다.", pointException.getMessage());
    }

    @Test
    @DisplayName("장바구니 상품 리스트 조회")
    void findCartByMemberId() {
        // given
        final List<CartProductInfo> productInfos = List.of(
                CartProductInfo.builder()
                               .productInfo(ProductInfo.builder()
                                                       .name("상품 A")
                                                       .build())
                               .build(),
                CartProductInfo.builder()
                               .productInfo(ProductInfo.builder()
                                                       .name("상품 B")
                                                       .build())
                               .build()
        );
        when(memberRepository.findCartProductsById(anyLong())).thenReturn(productInfos);

        // when
        List<CartProductInfo> result = memberService.findCartProductsByMemberId(1L);

        // then
        assertEquals(2, result.size());
        assertThat(result.stream()
                         .map(CartProductInfo::getProductInfo)
                         .map(ProductInfo::getName))
                .containsExactlyInAnyOrderElementsOf(List.of("상품 A", "상품 B"));

    }

    @Test
    @DisplayName("장바구니 조회 실패")
    void failToFindCartByMemberId() {
        // given
        when(memberRepository.findCartProductsById(anyLong())).thenThrow(new CartException(NO_SUCH_CART));

        // when
        CartException cartException = assertThrows(CartException.class, () -> memberService.findCartProductsByMemberId(1L));

        // then
        assertEquals(HttpStatus.BAD_REQUEST, cartException.getStatus());
        assertEquals("장바구니 정보가 없습니다.", cartException.getMessage());
    }

    @Test
    @DisplayName("장바구니 상품 추가")
    void addProductToCart() {
        // given
        CartAddCommand command = CartAddCommand.builder()
                                               .productId(1L)
                                               .cnt(1L)
                                               .build();
        CartInfo cartInfo = CartInfo.builder()
                                    .build();
        CartProductInfo productA = CartProductInfo.builder()
                                                  .productInfo(ProductInfo.builder()
                                                                          .name("상품 A")
                                                                          .build())
                                                  .build();
        when(memberRepository.findCartByMemberId(anyLong())).thenReturn(cartInfo);
        when(memberRepository.addCartByProductId(any(), any(), anyLong())).thenReturn(productA);

        // when
        List<CartProductInfo> result = memberService.addCartByProductId(1L, List.of(command));

        // then
        assertThat(result).containsExactlyInAnyOrderElementsOf(List.of(productA));
    }
}