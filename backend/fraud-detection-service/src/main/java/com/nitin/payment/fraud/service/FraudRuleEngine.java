/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.nitin.payment.common.event.TransactionCreatedEvent
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.stereotype.Component
 */
package com.nitin.payment.fraud.service;

import com.nitin.payment.common.event.TransactionCreatedEvent;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FraudRuleEngine {
    private final BigDecimal threshold;

    public FraudRuleEngine(@Value(value="${fraud.amount-threshold:50000}") BigDecimal threshold) {
        this.threshold = threshold;
    }

    public FraudDecision evaluate(TransactionCreatedEvent event) {
        int score = 10;
        String reason = "Normal transaction pattern";
        if (event.amount().compareTo(this.threshold) > 0) {
            score += 60;
            reason = "Amount greater than configured threshold";
        }
        if ("FAILED".equalsIgnoreCase(event.transactionStatus())) {
            score += 20;
            reason = "Failed transaction contributes to risk";
        }
        if (score >= 70) {
            return new FraudDecision(score, "HIGH_RISK", reason);
        }
        if (score >= 50) {
            return new FraudDecision(score, "MEDIUM_RISK", reason);
        }
        return new FraudDecision(score, "LOW_RISK", reason);
    }

    public record FraudDecision(int score, String status, String reason) {
    }
}
