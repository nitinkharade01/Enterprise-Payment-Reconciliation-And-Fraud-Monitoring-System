package com.nitin.payment.fraud.testdata;

import com.nitin.payment.common.event.TransactionCreatedEvent;
import com.nitin.payment.fraud.model.FraudAlert;
import java.math.BigDecimal;
import java.time.Instant;

public final class TestFraudAlertFactory {

    private TestFraudAlertFactory() {
    }

    public static TransactionCreatedEvent highValueEvent() {
        return new TransactionCreatedEvent("TXN-TEST-001", 2L, "Aarav Mehta", new BigDecimal("76000"), "INR", "UPI", "INITIATED", Instant.now());
    }

    public static FraudAlert alert() {
        FraudAlert alert = new FraudAlert();
        alert.setId("alert-1");
        alert.setTransactionId("TXN-TEST-001");
        alert.setUserId(2L);
        alert.setRiskScore(70);
        alert.setRiskStatus("HIGH_RISK");
        alert.setFraudReason("Amount greater than configured threshold");
        return alert;
    }
}
