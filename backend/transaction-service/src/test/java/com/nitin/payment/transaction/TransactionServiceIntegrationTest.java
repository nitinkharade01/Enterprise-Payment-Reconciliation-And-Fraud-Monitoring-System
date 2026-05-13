package com.nitin.payment.transaction;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TransactionServiceIntegrationTest {

    @Test
    void applicationClassIsRecoverable() {
        assertThat(TransactionServiceApplication.class).isNotNull();
    }
}
