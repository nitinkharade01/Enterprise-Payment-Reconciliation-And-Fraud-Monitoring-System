/*
 * Decompiled with CFR 0.152.
 */
package com.nitin.payment.transaction.dto;

import com.nitin.payment.transaction.entity.ReconciliationStatus;
import com.nitin.payment.transaction.entity.RiskStatus;
import com.nitin.payment.transaction.entity.TransactionStatus;
import java.math.BigDecimal;
import java.time.Instant;

public record TransactionResponse(Long id, String transactionId, Long userId, String customerName, String sourceAccount, String destinationAccount, BigDecimal amount, String currency, String paymentMode, TransactionStatus transactionStatus, RiskStatus riskStatus, ReconciliationStatus reconciliationStatus, String remarks, Instant createdAt, Instant updatedAt) {
}
