package com.cfbp.paymentreconciliation;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class PaymentReconciliationApplicationTests {

    @Test
    void contextLoadsWithKafkaAndRedisDisabled() {
    }
}
