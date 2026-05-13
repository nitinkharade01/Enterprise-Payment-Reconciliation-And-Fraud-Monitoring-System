/*
 * Decompiled with CFR 0.152.
 */
package com.nitin.payment.common.event;

import java.time.Instant;

public record ReconciliationCompletedEvent(Long fileId, long matchedCount, long mismatchedCount, long missingCount, Instant completedAt) {
}
