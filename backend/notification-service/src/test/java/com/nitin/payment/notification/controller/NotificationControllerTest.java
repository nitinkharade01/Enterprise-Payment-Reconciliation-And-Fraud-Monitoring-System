package com.nitin.payment.notification.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.nitin.payment.common.ApiResponse;
import com.nitin.payment.notification.service.NotificationService;
import com.nitin.payment.notification.testdata.TestNotificationFactory;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class NotificationControllerTest {

    @Test
    void allReturnsApiResponse() {
        NotificationService service = Mockito.mock(NotificationService.class);
        NotificationController controller = new NotificationController(service);
        when(service.all()).thenReturn(List.of(TestNotificationFactory.notification()));

        ApiResponse<?> response = controller.all();

        assertThat(response.status()).isEqualTo(200);
    }
}
