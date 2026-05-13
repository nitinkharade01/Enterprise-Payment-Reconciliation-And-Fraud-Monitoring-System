/*
 * Decompiled with CFR 0.152.
 */
package com.nitin.payment.common.event;

import java.time.Instant;

public record FraudAlertEvent(String transactionId, Long userId, int riskScore, String riskStatus, String fraudReason, Instant createdAt) {
}
