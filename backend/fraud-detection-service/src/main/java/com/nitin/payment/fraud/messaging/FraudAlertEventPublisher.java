/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.nitin.payment.common.event.TransactionCreatedEvent
 */
package com.nitin.payment.fraud.messaging;

import com.nitin.payment.common.event.TransactionCreatedEvent;
import com.nitin.payment.fraud.service.FraudRuleEngine;

public interface FraudAlertEventPublisher {
    public void publishFraudAlert(TransactionCreatedEvent var1, FraudRuleEngine.FraudDecision var2);
}
