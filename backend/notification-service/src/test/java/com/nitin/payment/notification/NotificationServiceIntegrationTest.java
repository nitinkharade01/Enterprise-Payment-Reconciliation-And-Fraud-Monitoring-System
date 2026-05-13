package com.nitin.payment.notification;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class NotificationServiceIntegrationTest {

    @Test
    void applicationClassIsRecoverable() {
        assertThat(NotificationServiceApplication.class).isNotNull();
    }
}
