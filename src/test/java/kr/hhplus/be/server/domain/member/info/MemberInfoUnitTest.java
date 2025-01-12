package kr.hhplus.be.server.domain.member.info;

import kr.hhplus.be.server.domain.member.exception.PointException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class MemberInfoUnitTest {

    @Test
    @DisplayName("유저 포인트 충전 금액 정상 반영")
    void chargePoint() {
        // given
        final Long BASE_POINT = 10_000L;
        final Long CHARGE_POINT = 20_000L;
        MemberInfo info = MemberInfo.builder()
                                    .point(BASE_POINT)
                                    .build();

        // when
        info.chargePoint(CHARGE_POINT);

        // then
        assertEquals(BASE_POINT, info.getPoint());
        assertEquals(BASE_POINT + CHARGE_POINT, info.getAfterPoint());
    }

    @Test
    @DisplayName("유저 포인트 사용 금액 정상 반영")
    void usePoint() {
        // given
        final Long BASE_POINT = 10_000L;
        final Long USE_POINT = 5_000L;
        MemberInfo info = MemberInfo.builder()
                                    .point(BASE_POINT)
                                    .build();

        // when
        info.usePoint(USE_POINT);

        // then
        assertEquals(BASE_POINT, info.getPoint());
        assertEquals(BASE_POINT - USE_POINT, info.getAfterPoint());
    }

    @Test
    @DisplayName("유저 잔여 포인트 부족")
    void insufficientBalance() {
        // given
        final Long BASE_POINT = 10_000L;
        final Long USE_POINT = 20_000L;
        MemberInfo info = MemberInfo.builder()
                                    .point(BASE_POINT)
                                    .build();

        // when
        PointException pointException = assertThrows(PointException.class, () -> info.usePoint(USE_POINT));

        // then
        assertEquals(HttpStatus.BAD_REQUEST, pointException.getStatus());
        assertEquals("포인트가 부족 합니다.", pointException.getMessage());
    }
}