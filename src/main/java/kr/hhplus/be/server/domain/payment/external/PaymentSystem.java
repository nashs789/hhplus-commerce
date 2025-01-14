package kr.hhplus.be.server.domain.payment.external;

public interface PaymentSystem {

    boolean sendPayment(Long memberId, Long usePoint);
}
