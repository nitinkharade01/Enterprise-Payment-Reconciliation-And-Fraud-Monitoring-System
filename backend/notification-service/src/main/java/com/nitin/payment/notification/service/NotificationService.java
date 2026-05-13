/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.nitin.payment.common.event.FraudAlertEvent
 *  com.nitin.payment.common.event.ReconciliationCompletedEvent
 *  com.nitin.payment.common.event.TransactionCreatedEvent
 *  com.nitin.payment.common.exception.ResourceNotFoundException
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.stereotype.Service
 */
package com.nitin.payment.notification.service;

import com.nitin.payment.common.event.FraudAlertEvent;
import com.nitin.payment.common.event.ReconciliationCompletedEvent;
import com.nitin.payment.common.event.TransactionCreatedEvent;
import com.nitin.payment.common.exception.ResourceNotFoundException;
import com.nitin.payment.notification.entity.NotificationLog;
import com.nitin.payment.notification.repository.NotificationRepository;
import java.util.List;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final NotificationRepository repository;

    public void recordFraudAlert(FraudAlertEvent event) {
        this.save("risk@payment.com", "FRAUD_ALERT", "Fraud alert: " + event.riskStatus(), event.fraudReason());
    }

    public void recordTransactionStatus(TransactionCreatedEvent event) {
        this.save("user-" + event.userId(), "PAYMENT_" + event.transactionStatus(), "Payment status updated", event.transactionId());
    }

    public void recordReconciliationCompleted(ReconciliationCompletedEvent event) {
        this.save("finance@payment.com", "RECONCILIATION_COMPLETED", "Reconciliation completed", "File " + event.fileId());
    }

    public List<NotificationLog> all() {
        return this.repository.findAll();
    }

    public NotificationLog markRead(Long id) {
        NotificationLog log = this.repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Notification not found: " + id));
        log.setRead(true);
        return this.repository.save(log);
    }

    private void save(String recipient, String type, String subject, String message) {
        NotificationLog logEntry = new NotificationLog();
        logEntry.setRecipient(recipient);
        logEntry.setType(type);
        logEntry.setSubject(subject);
        logEntry.setMessage(message);
        this.repository.save(logEntry);
        log.info("Mock notification stored type={} recipient={}", (Object)type, (Object)recipient);
    }

    @Generated
    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }
}
