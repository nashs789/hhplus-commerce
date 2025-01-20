package kr.hhplus.be.server.domain.payment.service;

import kr.hhplus.be.server.domain.order.info.OrderInfo;
import kr.hhplus.be.server.domain.payment.info.PaymentInfo;
import kr.hhplus.be.server.domain.payment.repository.PaymentRepository;
import kr.hhplus.be.server.infra.payment.entity.Payment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static kr.hhplus.be.server.infra.payment.entity.Payment.PaymentStatus.FAIL;
import static kr.hhplus.be.server.infra.payment.entity.Payment.PaymentStatus.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceUnitTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    @DisplayName("결제 저장")
    void savePayment() {
        // given
        final boolean isPayOk = true;
        final PaymentInfo paymentInfo = PaymentInfo.builder()
                                                   .paymentStatus(isPayOk ? SUCCESS : FAIL)
                                                   .build();
        when(paymentRepository.savePaymentResult(anyBoolean(), any())).thenReturn(paymentInfo);

        // when
        PaymentInfo result = paymentService.savePaymentResult(isPayOk, OrderInfo.builder()
                                                                                .build());

        // then
        assertEquals(paymentInfo, result);
        assertEquals(paymentInfo.getPaymentStatus(), result.getPaymentStatus());
    }
}