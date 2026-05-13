/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.nitin.payment.common.event.ReconciliationCompletedEvent
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
 *  org.springframework.kafka.core.KafkaTemplate
 *  org.springframework.stereotype.Service
 */
package com.nitin.payment.reconciliation.messaging;

import com.nitin.payment.common.event.ReconciliationCompletedEvent;
import com.nitin.payment.reconciliation.entity.ReconciliationSummary;
import com.nitin.payment.reconciliation.messaging.ReconciliationEventPublisher;
import java.time.Instant;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(prefix="app.kafka", name={"enabled"}, havingValue="true", matchIfMissing=true)
public class KafkaReconciliationEventPublisher
implements ReconciliationEventPublisher {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(KafkaReconciliationEventPublisher.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publishCompleted(Long fileId, ReconciliationSummary summary) {
        this.kafkaTemplate.send("reconciliation-completed-topic", fileId.toString(), new ReconciliationCompletedEvent(fileId, summary.getMatchedCount(), summary.getMismatchedCount(), summary.getMissingCount(), Instant.now()));
        log.info("Published reconciliation-completed event fileId={}", (Object)fileId);
    }

    @Generated
    public KafkaReconciliationEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
}
