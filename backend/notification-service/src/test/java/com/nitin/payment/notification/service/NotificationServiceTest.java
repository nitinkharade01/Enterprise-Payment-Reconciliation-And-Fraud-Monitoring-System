package com.nitin.payment.notification.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nitin.payment.notification.entity.NotificationLog;
import com.nitin.payment.notification.repository.NotificationRepository;
import com.nitin.payment.notification.testdata.TestNotificationFactory;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository repository;

    @Test
    void recordFraudAlertStoresNotification() {
        NotificationService service = new NotificationService(repository);

        service.recordFraudAlert(TestNotificationFactory.fraudAlertEvent());

        verify(repository).save(any(NotificationLog.class));
    }

    @Test
    void markReadUpdatesNotification() {
        NotificationLog log = TestNotificationFactory.notification();
        NotificationService service = new NotificationService(repository);
        when(repository.findById(1L)).thenReturn(Optional.of(log));
        when(repository.save(log)).thenReturn(log);

        NotificationLog updated = service.markRead(1L);

        assertThat(updated.isRead()).isTrue();
    }
}
