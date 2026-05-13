package com.nitin.payment.notification.testdata;

import com.nitin.payment.common.event.FraudAlertEvent;
import com.nitin.payment.common.event.ReconciliationCompletedEvent;
import com.nitin.payment.common.event.TransactionCreatedEvent;
import com.nitin.payment.notification.entity.NotificationLog;
import java.math.BigDecimal;
import java.time.Instant;

public final class TestNotificationFactory {

    private TestNotificationFactory() {
    }

    public static NotificationLog notification() {
        NotificationLog log = new NotificationLog();
        log.setId(1L);
        log.setRecipient("finance@payment.com");
        log.setType("RECONCILIATION_COMPLETED");
        log.setSubject("Reconciliation completed");
        log.setMessage("File 10");
        return log;
    }

    public static TransactionCreatedEvent transactionEvent() {
        return new TransactionCreatedEvent("TXN-TEST-001", 2L, "Aarav", new BigDecimal("1000"), "INR", "UPI", "SUCCESS", Instant.now());
    }

    public static FraudAlertEvent fraudAlertEvent() {
        return new FraudAlertEvent("TXN-TEST-001", 2L, 70, "HIGH_RISK", "Amount greater than threshold", Instant.now());
    }

    public static ReconciliationCompletedEvent reconciliationEvent() {
        return new ReconciliationCompletedEvent(10L, 1, 1, 0, Instant.now());
    }
}
