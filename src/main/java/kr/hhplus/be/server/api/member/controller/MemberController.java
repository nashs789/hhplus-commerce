package kr.hhplus.be.server.api.member.controller;

import kr.hhplus.be.server.api.member.request.PointChargeRequest;
import kr.hhplus.be.server.api.member.request.PointUseRequest;
import kr.hhplus.be.server.api.member.response.MemberResponse;
import kr.hhplus.be.server.api.member.response.PointHistoryResponse;
import kr.hhplus.be.server.domain.member.command.PointChargeCommand;
import kr.hhplus.be.server.domain.member.command.PointUseCommand;
import kr.hhplus.be.server.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static kr.hhplus.be.server.infra.member.entity.PointHistory.PointUseType.*;

@RestController
@RequiredArgsConstructor
public class MemberController implements MemberApi {

    private final MemberService memberService;

    @Override
    public ResponseEntity<MemberResponse> findMemberById(@PathVariable final Long memberId) {
        return ResponseEntity.ok(memberService.findMemberPointById(memberId)
                                              .toResponse());
    }

    @Override
    public ResponseEntity<PointHistoryResponse> chargePointById(
            @PathVariable final Long memberId,
            @RequestBody final PointChargeRequest pointChargeRequest
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
            @RequestBody final PointUseRequest pointUseRequest
    ) {
        return ResponseEntity.ok(memberService.useMemberPoint(PointUseCommand.builder()
                                                                             .memberId(memberId)
                                                                             .usePoint(pointUseRequest.usePoint())
                                                                             .pointUseType(USE)
                                                                             .build())
                                              .toResponse());
    }
}
