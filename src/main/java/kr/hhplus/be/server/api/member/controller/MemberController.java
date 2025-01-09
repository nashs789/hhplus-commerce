package kr.hhplus.be.server.api.member.controller;

import jakarta.validation.Valid;
import kr.hhplus.be.server.api.member.request.CartAddRequest;
import kr.hhplus.be.server.api.member.request.CartDeleteRequest;
import kr.hhplus.be.server.api.member.request.PointChargeRequest;
import kr.hhplus.be.server.api.member.request.PointUseRequest;
import kr.hhplus.be.server.api.member.response.CartProductResponse;
import kr.hhplus.be.server.api.member.response.MemberResponse;
import kr.hhplus.be.server.api.member.response.PointHistoryResponse;
import kr.hhplus.be.server.domain.member.command.CartAddCommand;
import kr.hhplus.be.server.domain.member.command.CartDeleteCommand;
import kr.hhplus.be.server.domain.member.command.PointChargeCommand;
import kr.hhplus.be.server.domain.member.command.PointUseCommand;
import kr.hhplus.be.server.domain.member.info.CartProductInfo;
import kr.hhplus.be.server.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static kr.hhplus.be.server.infra.member.entity.PointHistory.PointUseType.CHARGE;
import static kr.hhplus.be.server.infra.member.entity.PointHistory.PointUseType.USE;

@RestController
@RequiredArgsConstructor
public class MemberController implements MemberApi {

    private final MemberService memberService;

    @Override
    public ResponseEntity<MemberResponse> findMemberById(@PathVariable final Long memberId) {
        return ResponseEntity.ok(memberService.findMemberById(memberId)
                                              .toResponse());
    }

    @Override
    public ResponseEntity<PointHistoryResponse> chargePointById(
            @PathVariable final Long memberId,
            @Valid @RequestBody final PointChargeRequest pointChargeRequest
            ) {
        return ResponseEntity.ok(memberService.chargeMemberPoint(PointChargeCommand.builder()
                                                                                   .memberId(memberId)
                                                                                   .chargePoint(pointChargeRequest.chargePoint())
                                                                                   .pointUseType(CHARGE)
                                                                                   .build())
                                              .toResponse());
    }

    @Override
    public ResponseEntity<PointHistoryResponse> userPointById(
            @PathVariable final Long memberId,
            @Valid @RequestBody final PointUseRequest pointUseRequest
    ) {
        return ResponseEntity.ok(memberService.useMemberPoint(PointUseCommand.builder()
                                                                             .memberId(memberId)
                                                                             .usePoint(pointUseRequest.usePoint())
                                                                             .pointUseType(USE)
                                                                             .build())
                                              .toResponse());
    }

    @Override
    public ResponseEntity<List<CartProductResponse>> findCartItemsByMemberId(
            @PathVariable final Long memberId
    ) {
        return ResponseEntity.ok(memberService.findCartItemsByMemberId(memberId)
                                              .stream()
                                              .map(CartProductInfo::toResponse)
                                              .toList());
    }

    @Override
    public ResponseEntity<List<CartProductResponse>> addCartByProductId(
            @PathVariable Long memberId,
            @RequestBody List<CartAddRequest> cartAddRequests) {

        List<CartAddCommand> commands = cartAddRequests.stream()
                                                       .flatMap(cartAddRequest -> cartAddRequest.products()
                                                                                                .stream())
                                                       .map(product -> CartAddCommand.builder()
                                                                                     .productId(product.productId())
                                                                                     .cnt(product.cnt())
                                                                                     .build())
                                                       .toList();

        return ResponseEntity.ok(memberService.addCartByProductId(memberId, commands)
                                              .stream()
                                              .map(CartProductInfo::toResponse)
                                              .toList());
    }

    @Override
    public ResponseEntity<?> deleteCartById(
            @PathVariable Long memberId,
            @RequestBody List<CartDeleteRequest> cartDeleteRequests) {

        List<CartDeleteCommand> commands = cartDeleteRequests.stream()
                                                             .flatMap(cartDeleteRequest -> cartDeleteRequest.products()
                                                                                                            .stream())
                                                             .map(product -> CartDeleteCommand.builder()
                                                                                              .cartProductId(product.cartProductId())
                                                                                              .build())
                                                             .toList();

        memberService.deleteCartByProductId(memberId, commands);

        return ResponseEntity.status(HttpStatus.OK)
                             .body(null);
    }
}
