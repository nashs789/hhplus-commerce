package kr.hhplus.be.server.domain.member.service;

import kr.hhplus.be.server.domain.member.command.CartAddCommand;
import kr.hhplus.be.server.domain.member.command.CartDeleteCommand;
import kr.hhplus.be.server.domain.member.command.PointChargeCommand;
import kr.hhplus.be.server.domain.member.command.PointUseCommand;
import kr.hhplus.be.server.domain.member.exception.PointException;
import kr.hhplus.be.server.domain.member.info.CartInfo;
import kr.hhplus.be.server.domain.member.info.CartProductInfo;
import kr.hhplus.be.server.domain.member.info.MemberInfo;
import kr.hhplus.be.server.domain.member.info.PointHistoryInfo;
import kr.hhplus.be.server.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    @Transactional(readOnly = true)
    public CartInfo findCartByMemberId(final Long memberId) {
        return memberRepository.findCartByMemberId(memberId);
    }

    @Transactional(readOnly = true)
    public List<CartProductInfo> findCartProductsByMemberId(final Long memberId) {
        return memberRepository.findCartProductsById(memberId);
    }
    @Transactional
    public List<CartProductInfo> findCartProductsByMemberIdWithLock(final Long memberId) {
        return memberRepository.findCartProductsByMemberIdWithLock(memberId);
    }

    @Transactional
    public List<CartProductInfo> addCartByProductId(final Long memberId, final List<CartAddCommand> cartAddCommand) {
        List<CartProductInfo> result = new ArrayList<>();
        CartInfo cartInfo = memberRepository.findCartByMemberId(memberId);

        for(CartAddCommand addCommand : cartAddCommand) {
            result.add(
                    memberRepository.addCartByProductId(cartInfo, addCommand.getProductId(), addCommand.getCnt())
            );
        }

        return result;
    }

    @Transactional
    public void deleteCartByProductId(final Long memberId, final List<CartDeleteCommand> cartDeleteCommand) {
        for(CartDeleteCommand deleteCommand : cartDeleteCommand) {
            memberRepository.deleteCartByProductId(deleteCommand.getCartProductId());
        }
    }
}
