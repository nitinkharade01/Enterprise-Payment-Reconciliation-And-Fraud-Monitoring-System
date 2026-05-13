/*
 * Decompiled with CFR 0.152.
 */
package com.nitin.payment.common.event;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionCreatedEvent(String transactionId, Long userId, String customerName, BigDecimal amount, String currency, String paymentMode, String transactionStatus, Instant createdAt) {
}
