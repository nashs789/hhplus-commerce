package kr.hhplus.be.server.domain.member.service;

import kr.hhplus.be.server.domain.member.command.PointChargeCommand;
import kr.hhplus.be.server.domain.member.command.PointUseCommand;
import kr.hhplus.be.server.domain.member.exception.MemberException;
import kr.hhplus.be.server.domain.member.exception.PointException;
import kr.hhplus.be.server.domain.member.info.MemberInfo;
import kr.hhplus.be.server.domain.member.info.PointHistoryInfo;
import kr.hhplus.be.server.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static kr.hhplus.be.server.domain.member.exception.MemberException.MemberExceptionCode.NO_SUCH_MEMBER;
import static kr.hhplus.be.server.domain.member.exception.PointException.PointExceptionCode.FAIL_CHARGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        assertEquals(HttpStatus.NO_CONTENT, memberException.getStatus());
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
        assertEquals(pointHistoryInfo.getBeforeAmount(), BASE_POINT);
        assertEquals(pointHistoryInfo.getAfterAmount(), AFTER_POINT);
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
        assertEquals(pointHistoryInfo.getBeforeAmount(), BASE_POINT);
        assertEquals(pointHistoryInfo.getAfterAmount(), AFTER_POINT);
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
}