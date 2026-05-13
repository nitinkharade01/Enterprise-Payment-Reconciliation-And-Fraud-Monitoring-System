/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.nitin.payment.common.event.FraudAlertEvent
 *  com.nitin.payment.common.event.ReconciliationCompletedEvent
 *  com.nitin.payment.common.event.TransactionCreatedEvent
 *  lombok.Generated
 *  org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
 *  org.springframework.kafka.annotation.KafkaListener
 *  org.springframework.stereotype.Component
 */
package com.nitin.payment.notification.messaging;

import com.nitin.payment.common.event.FraudAlertEvent;
import com.nitin.payment.common.event.ReconciliationCompletedEvent;
import com.nitin.payment.common.event.TransactionCreatedEvent;
import com.nitin.payment.notification.service.NotificationService;
import lombok.Generated;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix="app.kafka", name={"enabled"}, havingValue="true", matchIfMissing=true)
public class NotificationEventListener {
    private final NotificationService notificationService;

    @KafkaListener(topics={"fraud-alert-topic"}, groupId="notification-service")
    public void fraud(FraudAlertEvent event) {
        this.notificationService.recordFraudAlert(event);
    }

    @KafkaListener(topics={"transaction-status-updated-topic"}, groupId="notification-service")
    public void transaction(TransactionCreatedEvent event) {
        this.notificationService.recordTransactionStatus(event);
    }

    @KafkaListener(topics={"reconciliation-completed-topic"}, groupId="notification-service")
    public void reconciliation(ReconciliationCompletedEvent event) {
        this.notificationService.recordReconciliationCompleted(event);
    }

    @Generated
    public NotificationEventListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
}
