package kr.hhplus.be.server.infra.member.repository.impl;

import kr.hhplus.be.server.domain.member.command.PointChargeCommand;
import kr.hhplus.be.server.domain.member.command.PointUseCommand;
import kr.hhplus.be.server.domain.member.exception.CartException;
import kr.hhplus.be.server.domain.member.exception.MemberException;
import kr.hhplus.be.server.domain.member.info.CartInfo;
import kr.hhplus.be.server.domain.member.info.CartProductInfo;
import kr.hhplus.be.server.domain.member.info.MemberInfo;
import kr.hhplus.be.server.domain.member.info.PointHistoryInfo;
import kr.hhplus.be.server.domain.member.repository.MemberRepository;
import kr.hhplus.be.server.infra.member.entity.Cart;
import kr.hhplus.be.server.infra.member.entity.CartProduct;
import kr.hhplus.be.server.infra.member.entity.Member;
import kr.hhplus.be.server.infra.member.entity.PointHistory;
import kr.hhplus.be.server.infra.member.repository.CartJpaRepository;
import kr.hhplus.be.server.infra.member.repository.CartProductJpaRepository;
import kr.hhplus.be.server.infra.member.repository.MemberJpaRepository;
import kr.hhplus.be.server.infra.member.repository.PointHistoryJpaRepository;
import kr.hhplus.be.server.infra.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.hhplus.be.server.domain.member.exception.CartException.CartExceptionCode.NO_SUCH_CART;
import static kr.hhplus.be.server.domain.member.exception.MemberException.MemberExceptionCode.NO_SUCH_MEMBER;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;
    private final PointHistoryJpaRepository pointHistoryJpaRepository;
    private final CartJpaRepository cartJpaRepository;
    private final CartProductJpaRepository cartProductJpaRepository;

    @Override
    public MemberInfo saveMember(MemberInfo memberInfo) {
        return memberJpaRepository.save(Member.builder()
                                              .point(memberInfo.getPoint())
                                              .build())
                                  .toInfo();
    }

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

    @Override
    public CartInfo findCartByMemberId(final Long memberId) {
        return cartJpaRepository.findCartByMemberId(memberId)
                                .orElseThrow(() -> new CartException(NO_SUCH_CART))
                                .toInfo();
    }

    @Override
    public List<CartProductInfo> findCartProductsById(final Long memberId) {
        return cartProductJpaRepository.findCartProductsByMemberId(memberId)
                                       .orElseThrow(() -> new CartException(NO_SUCH_CART))
                                       .stream()
                                       .map(CartProduct::toInfo)
                                       .toList();
    }

    @Override
    public List<CartProductInfo> findCartProductsByMemberIdWithLock(final Long memberId) {
        return cartProductJpaRepository.findCartProductsByMemberIdWithLock(memberId)
                                       .orElseThrow(() -> new CartException(NO_SUCH_CART))
                                       .stream()
                                       .map(CartProduct::toInfo)
                                       .toList();
    }

    @Override
    public CartProductInfo addCartByProductId(final CartInfo cartInfo, final Long productId, final Long quantity) {
        Cart cart = Cart.builder()
                        .id(cartInfo.getId())
                        .member(Member.builder()
                                      .id(cartInfo.getMemberInfo().getId())
                                      .build())
                        .build();
        Product product = Product.builder()
                                 .id(productId)
                                 .build();
        CartProduct cartProduct = CartProduct.builder()
                                             .cart(cart)
                                             .product(product)
                                             .quantity(quantity)
                                             .build();

        return cartProductJpaRepository.save(cartProduct)
                                       .toInfo();
    }

    @Override
    public void deleteCartByProductId(final Long cartProductId) {
        cartProductJpaRepository.deleteById(cartProductId);
    }
}
