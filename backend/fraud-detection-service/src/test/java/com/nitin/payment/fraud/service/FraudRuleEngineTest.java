package com.nitin.payment.fraud.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.nitin.payment.fraud.testdata.TestFraudAlertFactory;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class FraudRuleEngineTest {

    @Test
    void highValuePaymentIsHighRisk() {
        FraudRuleEngine engine = new FraudRuleEngine(new BigDecimal("50000"));

        FraudRuleEngine.FraudDecision decision = engine.evaluate(TestFraudAlertFactory.highValueEvent());

        assertThat(decision.status()).isEqualTo("HIGH_RISK");
        assertThat(decision.score()).isGreaterThanOrEqualTo(70);
    }
}
