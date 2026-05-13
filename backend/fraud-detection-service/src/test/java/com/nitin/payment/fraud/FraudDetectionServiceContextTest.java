package com.nitin.payment.fraud;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class FraudDetectionServiceContextTest {

    @Test
    void applicationClassIsRecoverable() {
        assertThat(FraudDetectionServiceApplication.class).isNotNull();
    }
}
