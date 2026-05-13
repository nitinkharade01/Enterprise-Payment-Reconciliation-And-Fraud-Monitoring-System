package com.nitin.payment.discovery;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DiscoveryServerApplicationTest {

    @Test
    void applicationClassIsRecoverable() {
        assertThat(DiscoveryServerApplication.class).isNotNull();
    }
}
