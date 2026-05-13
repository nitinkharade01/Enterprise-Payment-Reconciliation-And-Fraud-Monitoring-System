package com.nitin.payment.fraud.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nitin.payment.fraud.messaging.FraudAlertEventPublisher;
import com.nitin.payment.fraud.repository.FraudAlertRepository;
import com.nitin.payment.fraud.testdata.TestFraudAlertFactory;
import com.nitin.payment.common.event.TransactionCreatedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FraudAlertServiceTest {

    @Mock
    private FraudAlertRepository repository;
    @Mock
    private FraudRuleEngine ruleEngine;
    @Mock
    private FraudAlertEventPublisher eventPublisher;

    @Test
    void processCreatesAlertWhenDecisionIsNotLowRisk() {
        FraudAlertService service = new FraudAlertService(repository, ruleEngine, eventPublisher);
        TransactionCreatedEvent event = TestFraudAlertFactory.highValueEvent();
        when(ruleEngine.evaluate(event))
                .thenReturn(new FraudRuleEngine.FraudDecision(70, "HIGH_RISK", "Amount greater than configured threshold"));
        when(repository.save(any())).thenReturn(TestFraudAlertFactory.alert());

        service.process(event);

        verify(repository).save(any());
        verify(eventPublisher).publishFraudAlert(any(), any());
    }
}
