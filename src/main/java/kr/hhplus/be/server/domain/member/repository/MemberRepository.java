package kr.hhplus.be.server.domain.member.repository;

import kr.hhplus.be.server.domain.member.command.PointChargeCommand;
import kr.hhplus.be.server.domain.member.command.PointUseCommand;
import kr.hhplus.be.server.domain.member.info.CartInfo;
import kr.hhplus.be.server.domain.member.info.CartProductInfo;
import kr.hhplus.be.server.domain.member.info.MemberInfo;
import kr.hhplus.be.server.domain.member.info.PointHistoryInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository {

    MemberInfo saveMember(MemberInfo memberInfo);
    MemberInfo findMemberById(Long memberId);
    MemberInfo findByMemberIdWithLock(Long memberId);
    boolean chargeMemberPoint(MemberInfo memberInfo);
    boolean useMemberPoint(MemberInfo memberInfo);
    PointHistoryInfo savePointChargeHistory(MemberInfo memberInfo, PointChargeCommand pointChargeCommand);
    PointHistoryInfo savePointUseHistory(MemberInfo memberInfo, PointUseCommand pointUseCommand);
    CartInfo findCartByMemberId(Long memberId);
    List<CartProductInfo> findCartProductsById(Long memberId);
    List<CartProductInfo> findCartProductsByMemberIdWithLock(Long memberId);
    CartProductInfo addCartByProductId(CartInfo cartInfo, Long productId, Long cnt);
    void deleteCartByProductId(Long cartProductId);
}
