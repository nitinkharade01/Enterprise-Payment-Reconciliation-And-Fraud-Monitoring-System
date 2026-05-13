/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.nitin.payment.common.event.TransactionCreatedEvent
 *  lombok.Generated
 *  org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
 *  org.springframework.kafka.annotation.KafkaListener
 *  org.springframework.stereotype.Component
 */
package com.nitin.payment.fraud.messaging;

import com.nitin.payment.common.event.TransactionCreatedEvent;
import com.nitin.payment.fraud.service.FraudAlertService;
import lombok.Generated;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix="app.kafka", name={"enabled"}, havingValue="true", matchIfMissing=true)
public class FraudTransactionEventListener {
    private final FraudAlertService fraudAlertService;

    @KafkaListener(topics={"transaction-created-topic"}, groupId="fraud-detection-service")
    public void consume(TransactionCreatedEvent event) {
        this.fraudAlertService.process(event);
    }

    @Generated
    public FraudTransactionEventListener(FraudAlertService fraudAlertService) {
        this.fraudAlertService = fraudAlertService;
    }
}
