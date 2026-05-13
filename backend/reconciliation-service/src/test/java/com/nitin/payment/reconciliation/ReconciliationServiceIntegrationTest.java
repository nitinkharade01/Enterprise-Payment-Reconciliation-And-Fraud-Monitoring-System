package com.nitin.payment.reconciliation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ReconciliationServiceIntegrationTest {

    @Test
    void applicationClassIsRecoverable() {
        assertThat(ReconciliationServiceApplication.class).isNotNull();
    }
}
