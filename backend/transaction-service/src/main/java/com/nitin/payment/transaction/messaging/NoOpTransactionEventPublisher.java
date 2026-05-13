/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
 *  org.springframework.stereotype.Service
 */
package com.nitin.payment.transaction.messaging;

import com.nitin.payment.transaction.entity.PaymentTransaction;
import com.nitin.payment.transaction.messaging.TransactionEventPublisher;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(prefix="app.kafka", name={"enabled"}, havingValue="false")
public class NoOpTransactionEventPublisher
implements TransactionEventPublisher {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(NoOpTransactionEventPublisher.class);

    @Override
    public void publishCreated(PaymentTransaction transaction) {
        log.debug("Kafka disabled; skipped transaction-created event transactionId={}", (Object)transaction.getTransactionId());
    }

    @Override
    public void publishStatusUpdated(PaymentTransaction transaction) {
        log.debug("Kafka disabled; skipped transaction-status-updated event transactionId={}", (Object)transaction.getTransactionId());
    }
}
