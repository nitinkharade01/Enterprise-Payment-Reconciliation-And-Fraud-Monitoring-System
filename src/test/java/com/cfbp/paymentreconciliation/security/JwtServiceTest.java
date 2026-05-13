package com.cfbp.paymentreconciliation.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

class JwtServiceTest {

    private final JwtService jwtService = new JwtService(
            "local-dev-secret-key-for-payment-project-please-change",
            86400000
    );

    @Test
    void generateTokenCreatesValidTokenForUser() {
        UserDetails userDetails = User.withUsername("admin@payment.com")
                .password("encoded")
                .authorities("ROLE_ADMIN")
                .build();

        String token = jwtService.generateToken(userDetails);

        assertThat(jwtService.extractUsername(token)).isEqualTo("admin@payment.com");
        assertThat(jwtService.extractRoles(token)).contains("ROLE_ADMIN");
        assertThat(jwtService.isTokenValid(token, userDetails)).isTrue();
    }
}
