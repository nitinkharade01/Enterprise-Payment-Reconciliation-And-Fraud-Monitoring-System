/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.nitin.payment.common.event.TransactionCreatedEvent
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
 *  org.springframework.stereotype.Service
 */
package com.nitin.payment.fraud.messaging;

import com.nitin.payment.common.event.TransactionCreatedEvent;
import com.nitin.payment.fraud.messaging.FraudAlertEventPublisher;
import com.nitin.payment.fraud.service.FraudRuleEngine;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(prefix="app.kafka", name={"enabled"}, havingValue="false")
public class NoOpFraudAlertEventPublisher
implements FraudAlertEventPublisher {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(NoOpFraudAlertEventPublisher.class);

    @Override
    public void publishFraudAlert(TransactionCreatedEvent event, FraudRuleEngine.FraudDecision decision) {
        log.debug("Kafka disabled; skipped fraud-alert event transactionId={}", (Object)event.transactionId());
    }
}
