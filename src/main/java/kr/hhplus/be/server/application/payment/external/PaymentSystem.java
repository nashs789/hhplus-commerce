package kr.hhplus.be.server.application.payment.external;

public interface PaymentSystem {

    boolean sendPayment(Long memberId, Long usePoint);
}
