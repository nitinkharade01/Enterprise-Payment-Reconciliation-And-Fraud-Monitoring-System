/*
 * Decompiled with CFR 0.152.
 */
package com.nitin.payment.reconciliation.messaging;

import com.nitin.payment.reconciliation.entity.ReconciliationSummary;

public interface ReconciliationEventPublisher {
    public void publishCompleted(Long var1, ReconciliationSummary var2);
}
