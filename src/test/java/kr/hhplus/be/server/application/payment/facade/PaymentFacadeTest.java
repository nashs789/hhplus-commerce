package kr.hhplus.be.server.application.payment.facade;

import kr.hhplus.be.server.infra.member.repository.MemberJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.testcontainers.junit.jupiter.Testcontainers;

@Sql(
        scripts = "/test-member-data.sql",
        config = @SqlConfig(encoding = "UTF-8")
)
@SpringBootTest
@Testcontainers
class PaymentFacadeTest {

    @Autowired
    private PaymentFacade paymentFacade;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Test
    @DisplayName("결제 정상 진행")
    void test() {
        // given

        // when

        // then
    }
}