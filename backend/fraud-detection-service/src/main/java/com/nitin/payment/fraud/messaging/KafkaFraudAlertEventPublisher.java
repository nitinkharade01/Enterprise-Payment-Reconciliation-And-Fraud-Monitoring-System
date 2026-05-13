/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.nitin.payment.common.event.FraudAlertEvent
 *  com.nitin.payment.common.event.TransactionCreatedEvent
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
 *  org.springframework.kafka.core.KafkaTemplate
 *  org.springframework.stereotype.Service
 */
package com.nitin.payment.fraud.messaging;

import com.nitin.payment.common.event.FraudAlertEvent;
import com.nitin.payment.common.event.TransactionCreatedEvent;
import com.nitin.payment.fraud.messaging.FraudAlertEventPublisher;
import com.nitin.payment.fraud.service.FraudRuleEngine;
import java.time.Instant;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(prefix="app.kafka", name={"enabled"}, havingValue="true", matchIfMissing=true)
public class KafkaFraudAlertEventPublisher
implements FraudAlertEventPublisher {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(KafkaFraudAlertEventPublisher.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publishFraudAlert(TransactionCreatedEvent event, FraudRuleEngine.FraudDecision decision) {
        this.kafkaTemplate.send("fraud-alert-topic", event.transactionId(), new FraudAlertEvent(event.transactionId(), event.userId(), decision.score(), decision.status(), decision.reason(), Instant.now()));
        log.info("Published fraud-alert event transactionId={}", (Object)event.transactionId());
    }

    @Generated
    public KafkaFraudAlertEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
}
