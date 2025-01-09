package kr.hhplus.be.server.domain.member.repository;

import kr.hhplus.be.server.domain.member.command.PointChargeCommand;
import kr.hhplus.be.server.domain.member.command.PointUseCommand;
import kr.hhplus.be.server.domain.member.info.MemberInfo;
import kr.hhplus.be.server.domain.member.info.PointHistoryInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository {

    MemberInfo findMemberById(Long memberId);
    MemberInfo findByMemberIdWithLock(Long memberId);
    boolean chargeMemberPoint(MemberInfo memberInfo);
    boolean useMemberPoint(MemberInfo memberInfo);
    PointHistoryInfo savePointChargeHistory(MemberInfo memberInfo, PointChargeCommand pointChargeCommand);
    PointHistoryInfo savePointUseHistory(MemberInfo memberInfo, PointUseCommand pointUseCommand);
}
