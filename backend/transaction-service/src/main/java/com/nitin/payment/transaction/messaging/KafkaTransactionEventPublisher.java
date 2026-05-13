/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.nitin.payment.common.event.TransactionCreatedEvent
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
 *  org.springframework.kafka.core.KafkaTemplate
 *  org.springframework.stereotype.Service
 */
package com.nitin.payment.transaction.messaging;

import com.nitin.payment.common.event.TransactionCreatedEvent;
import com.nitin.payment.transaction.entity.PaymentTransaction;
import com.nitin.payment.transaction.messaging.TransactionEventPublisher;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(prefix="app.kafka", name={"enabled"}, havingValue="true", matchIfMissing=true)
public class KafkaTransactionEventPublisher
implements TransactionEventPublisher {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(KafkaTransactionEventPublisher.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publishCreated(PaymentTransaction transaction) {
        this.kafkaTemplate.send("transaction-created-topic", transaction.getTransactionId(), this.toEvent(transaction));
        log.info("Published transaction-created event transactionId={}", (Object)transaction.getTransactionId());
    }

    @Override
    public void publishStatusUpdated(PaymentTransaction transaction) {
        this.kafkaTemplate.send("transaction-status-updated-topic", transaction.getTransactionId(), this.toEvent(transaction));
        log.info("Published transaction-status-updated event transactionId={}", (Object)transaction.getTransactionId());
    }

    private TransactionCreatedEvent toEvent(PaymentTransaction transaction) {
        return new TransactionCreatedEvent(transaction.getTransactionId(), transaction.getUserId(), transaction.getCustomerName(), transaction.getAmount(), transaction.getCurrency(), transaction.getPaymentMode(), transaction.getTransactionStatus().name(), transaction.getCreatedAt());
    }

    @Generated
    public KafkaTransactionEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
}
