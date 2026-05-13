package com.nitin.payment.auth.security;

import static org.assertj.core.api.Assertions.assertThat;

import com.nitin.payment.auth.testdata.TestUserFactory;
import org.junit.jupiter.api.Test;

class JwtServiceTest {

    @Test
    void generateReturnsSignedToken() {
        JwtService jwtService = new JwtService("local-dev-secret-key-for-payment-project-please-change", 86400000);

        String token = jwtService.generate(TestUserFactory.user());

        assertThat(token).isNotBlank();
        assertThat(token.split("\\.")).hasSize(3);
    }
}
