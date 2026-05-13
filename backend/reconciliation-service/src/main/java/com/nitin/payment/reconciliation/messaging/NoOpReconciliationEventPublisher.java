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
package com.nitin.payment.reconciliation.messaging;

import com.nitin.payment.reconciliation.entity.ReconciliationSummary;
import com.nitin.payment.reconciliation.messaging.ReconciliationEventPublisher;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(prefix="app.kafka", name={"enabled"}, havingValue="false")
public class NoOpReconciliationEventPublisher
implements ReconciliationEventPublisher {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(NoOpReconciliationEventPublisher.class);

    @Override
    public void publishCompleted(Long fileId, ReconciliationSummary summary) {
        log.debug("Kafka disabled; skipped reconciliation-completed event fileId={}", (Object)fileId);
    }
}
