package com.nitin.payment.gateway.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class JwtAuthenticationFilterTest {

    @Test
    void filterHasHighPrecedence() {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter("local-dev-secret-key-for-payment-project-please-change");

        assertThat(filter.getOrder()).isEqualTo(-1);
    }
}
