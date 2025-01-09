package kr.hhplus.be.server.domain.member.service;

import kr.hhplus.be.server.domain.member.command.PointChargeCommand;
import kr.hhplus.be.server.domain.member.command.PointUseCommand;
import kr.hhplus.be.server.domain.member.exception.PointException;
import kr.hhplus.be.server.domain.member.info.MemberInfo;
import kr.hhplus.be.server.domain.member.info.PointHistoryInfo;
import kr.hhplus.be.server.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static kr.hhplus.be.server.domain.member.exception.PointException.PointExceptionCode.FAIL_CHARGE;
import static kr.hhplus.be.server.domain.member.exception.PointException.PointExceptionCode.FAIL_USE;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberInfo findMemberById(final Long memberId) {
        return memberRepository.findMemberById(memberId);
    }

    @Transactional
    public PointHistoryInfo chargeMemberPoint(final PointChargeCommand pointChargeCommand) {
        MemberInfo memberInfo = memberRepository.findByMemberIdWithLock(pointChargeCommand.getMemberId());

        memberInfo.chargePoint(pointChargeCommand.getChargePoint());

        if(memberRepository.chargeMemberPoint(memberInfo)) {
            return memberRepository.savePointChargeHistory(memberInfo, pointChargeCommand);
        }

        throw new PointException(FAIL_CHARGE);
    }

    @Transactional
    public PointHistoryInfo useMemberPoint(final PointUseCommand pointUseCommand) {
        MemberInfo memberInfo = memberRepository.findByMemberIdWithLock(pointUseCommand.getMemberId());

        memberInfo.usePoint(pointUseCommand.getUsePoint());

        if(memberRepository.useMemberPoint(memberInfo)) {
            return memberRepository.savePointUseHistory(memberInfo, pointUseCommand);
        }

        throw new PointException(FAIL_USE);
    }
}
