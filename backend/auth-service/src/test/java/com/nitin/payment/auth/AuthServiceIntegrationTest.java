package com.nitin.payment.auth;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AuthServiceIntegrationTest {

    @Test
    void applicationClassIsRecoverable() {
        assertThat(AuthServiceApplication.class).isNotNull();
    }
}
