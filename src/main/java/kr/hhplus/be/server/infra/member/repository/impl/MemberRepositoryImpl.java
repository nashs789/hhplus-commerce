package kr.hhplus.be.server.infra.member.repository.impl;

import kr.hhplus.be.server.domain.member.command.PointChargeCommand;
import kr.hhplus.be.server.domain.member.command.PointUseCommand;
import kr.hhplus.be.server.domain.member.exception.MemberException;
import kr.hhplus.be.server.domain.member.info.MemberInfo;
import kr.hhplus.be.server.domain.member.info.PointHistoryInfo;
import kr.hhplus.be.server.domain.member.repository.MemberRepository;
import kr.hhplus.be.server.infra.member.entity.Member;
import kr.hhplus.be.server.infra.member.entity.PointHistory;
import kr.hhplus.be.server.infra.member.repository.MemberJpaRepository;
import kr.hhplus.be.server.infra.member.repository.PointHistoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static kr.hhplus.be.server.domain.member.exception.MemberException.MemberExceptionCode.NO_SUCH_MEMBER;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;
    private final PointHistoryJpaRepository pointHistoryJpaRepository;

    @Override
    public MemberInfo findMemberById(final Long memberId) {
        return memberJpaRepository.findById(memberId)
                                  .orElseThrow(() -> new MemberException(NO_SUCH_MEMBER))
                                  .toInfo();
    }

    @Override
    public MemberInfo findByMemberIdWithLock(final Long memberId) {
        return memberJpaRepository.findByMemberIdWithLock(memberId)
                                  .orElseThrow(() -> new MemberException(NO_SUCH_MEMBER))
                                  .toInfo();
    }

    @Override
    public boolean chargeMemberPoint(final MemberInfo memberInfo) {
        return memberJpaRepository.chargeMemberPoint(memberInfo.getId(), memberInfo.getAfterPoint()) > 0;
    }

    @Override
    public boolean useMemberPoint(MemberInfo memberInfo) {
        return memberJpaRepository.useMemberPoint(memberInfo.getId(), memberInfo.getAfterPoint()) > 0;
    }

    @Override
    public PointHistoryInfo savePointChargeHistory(final MemberInfo memberInfo, final PointChargeCommand pointChargeCommand) {
        return pointHistoryJpaRepository.save(PointHistory.builder()
                                                          .member(Member.builder()
                                                                        .id(memberInfo.getId())
                                                                        .point(memberInfo.getAfterPoint())
                                                                        .createdAt(memberInfo.getCreateAt())
                                                                        .updatedAt(memberInfo.getUpdateAt())
                                                                        .build())
                                                          .afterAmount(memberInfo.getAfterPoint())
                                                          .beforeAmount(memberInfo.getPoint())
                                                          .pointUseType(pointChargeCommand.getPointUseType())
                                                          .build())
                                        .toInfo();
    }

    @Override
    public PointHistoryInfo savePointUseHistory(final MemberInfo memberInfo, final PointUseCommand pointUseCommand) {
        return pointHistoryJpaRepository.save(PointHistory.builder()
                                                          .member(Member.builder()
                                                                        .id(memberInfo.getId())
                                                                        .point(memberInfo.getAfterPoint())
                                                                        .createdAt(memberInfo.getCreateAt())
                                                                        .updatedAt(memberInfo.getUpdateAt())
                                                                        .build())
                                                          .afterAmount(memberInfo.getAfterPoint())
                                                          .beforeAmount(memberInfo.getPoint())
                                                          .pointUseType(pointUseCommand.getPointUseType())
                                                          .build())
                                        .toInfo();
    }
}
