package com.nitin.payment.gateway;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ApiGatewayApplicationTest {

    @Test
    void applicationClassIsRecoverable() {
        assertThat(ApiGatewayApplication.class).isNotNull();
    }
}
