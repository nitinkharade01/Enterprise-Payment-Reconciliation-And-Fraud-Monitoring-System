/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.nitin.payment.common.event.TransactionCreatedEvent
 *  com.nitin.payment.common.exception.ResourceNotFoundException
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.stereotype.Service
 */
package com.nitin.payment.fraud.service;

import com.nitin.payment.common.event.TransactionCreatedEvent;
import com.nitin.payment.common.exception.ResourceNotFoundException;
import com.nitin.payment.fraud.messaging.FraudAlertEventPublisher;
import com.nitin.payment.fraud.model.FraudAlert;
import com.nitin.payment.fraud.repository.FraudAlertRepository;
import com.nitin.payment.fraud.service.FraudRuleEngine;
import java.util.List;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FraudAlertService {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(FraudAlertService.class);
    private final FraudAlertRepository repository;
    private final FraudRuleEngine ruleEngine;
    private final FraudAlertEventPublisher eventPublisher;

    public void process(TransactionCreatedEvent event) {
        FraudRuleEngine.FraudDecision decision = this.ruleEngine.evaluate(event);
        if (!"LOW_RISK".equals(decision.status())) {
            FraudAlert alert = new FraudAlert();
            alert.setTransactionId(event.transactionId());
            alert.setUserId(event.userId());
            alert.setRiskScore(decision.score());
            alert.setRiskStatus(decision.status());
            alert.setFraudReason(decision.reason());
            FraudAlert saved = (FraudAlert)this.repository.save(alert);
            this.eventPublisher.publishFraudAlert(event, decision);
            log.info("Fraud alert created id={} transactionId={}", (Object)saved.getId(), (Object)event.transactionId());
        }
    }

    public List<FraudAlert> findAll() {
        return this.repository.findAll();
    }

    public FraudAlert find(String id) {
        return (FraudAlert)this.repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Fraud alert not found: " + id));
    }

    public FraudAlert updateStatus(String id, String status) {
        FraudAlert alert = this.find(id);
        alert.setAlertStatus(status);
        return (FraudAlert)this.repository.save(alert);
    }

    @Generated
    public FraudAlertService(FraudAlertRepository repository, FraudRuleEngine ruleEngine, FraudAlertEventPublisher eventPublisher) {
        this.repository = repository;
        this.ruleEngine = ruleEngine;
        this.eventPublisher = eventPublisher;
    }
}
