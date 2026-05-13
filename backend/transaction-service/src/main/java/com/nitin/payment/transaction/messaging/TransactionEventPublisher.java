/*
 * Decompiled with CFR 0.152.
 */
package com.nitin.payment.transaction.messaging;

import com.nitin.payment.transaction.entity.PaymentTransaction;

public interface TransactionEventPublisher {
    public void publishCreated(PaymentTransaction var1);

    public void publishStatusUpdated(PaymentTransaction var1);
}
