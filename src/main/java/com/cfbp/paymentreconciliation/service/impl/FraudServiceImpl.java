package com.cfbp.paymentreconciliation.service.impl;

import com.cfbp.paymentreconciliation.dto.FraudAlertResponse;
import com.cfbp.paymentreconciliation.entity.FraudAlert;
import com.cfbp.paymentreconciliation.entity.PaymentTransaction;
import com.cfbp.paymentreconciliation.enums.AuditAction;
import com.cfbp.paymentreconciliation.enums.FraudAlertStatus;
import com.cfbp.paymentreconciliation.enums.FraudSeverity;
import com.cfbp.paymentreconciliation.exception.ResourceNotFoundException;
import com.cfbp.paymentreconciliation.mapper.FraudAlertMapper;
import com.cfbp.paymentreconciliation.repository.FraudAlertRepository;
import com.cfbp.paymentreconciliation.repository.PaymentTransactionRepository;
import com.cfbp.paymentreconciliation.service.AuditService;
import com.cfbp.paymentreconciliation.service.EventPublisher;
import com.cfbp.paymentreconciliation.service.FraudService;
import com.cfbp.paymentreconciliation.util.CurrentUserUtil;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FraudServiceImpl implements FraudService {

    private static final BigDecimal HIGH_VALUE_LIMIT = new BigDecimal("100000");
    private static final BigDecimal MEDIUM_VALUE_LIMIT = new BigDecimal("50000");

    private final PaymentTransactionRepository paymentRepository;
    private final FraudAlertRepository fraudAlertRepository;
    private final FraudAlertMapper fraudAlertMapper;
    private final EventPublisher eventPublisher;
    private final AuditService auditService;
    private final String fraudAlertTopic;

    public FraudServiceImpl(PaymentTransactionRepository paymentRepository,
                            FraudAlertRepository fraudAlertRepository,
                            FraudAlertMapper fraudAlertMapper,
                            EventPublisher eventPublisher,
                            AuditService auditService,
                            @Value("${app.kafka.topics.fraud-alert}") String fraudAlertTopic) {
        this.paymentRepository = paymentRepository;
        this.fraudAlertRepository = fraudAlertRepository;
        this.fraudAlertMapper = fraudAlertMapper;
        this.eventPublisher = eventPublisher;
        this.auditService = auditService;
        this.fraudAlertTopic = fraudAlertTopic;
    }

    @Override
    @Transactional
    public FraudAlertResponse checkPayment(Long paymentId) {
        PaymentTransaction payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id " + paymentId));
        FraudAlert alert = evaluate(payment);
        FraudAlert saved = fraudAlertRepository.save(alert);
        if (saved.getSeverity() != FraudSeverity.LOW) {
            payment.setFraudFlagged(true);
            paymentRepository.save(payment);
        }
        FraudAlertResponse response = fraudAlertMapper.toResponse(saved);
        eventPublisher.publish(fraudAlertTopic, response);
        auditService.log(CurrentUserUtil.username(), AuditAction.FRAUD_ALERT_CREATED, "FraudAlert",
                String.valueOf(saved.getId()), saved.getDescription());
        return response;
    }

    @Override
    public List<FraudAlertResponse> getAlerts() {
        return fraudAlertRepository.findAll().stream()
                .sorted(Comparator.comparing(FraudAlert::getCreatedAt).reversed())
                .map(fraudAlertMapper::toResponse)
                .toList();
    }

    @Override
    public FraudAlertResponse getAlert(Long id) {
        FraudAlert alert = fraudAlertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fraud alert not found with id " + id));
        return fraudAlertMapper.toResponse(alert);
    }

    private FraudAlert evaluate(PaymentTransaction payment) {
        FraudAlert alert = new FraudAlert();
        alert.setPaymentTransaction(payment);

        if (payment.getAmount().compareTo(HIGH_VALUE_LIMIT) >= 0) {
            alert.setSeverity(FraudSeverity.HIGH);
            alert.setRuleName("HIGH_VALUE_PAYMENT");
            alert.setDescription("Payment amount exceeds high-value fraud threshold");
            return alert;
        }
        if (payment.getAmount().compareTo(MEDIUM_VALUE_LIMIT) >= 0) {
            alert.setSeverity(FraudSeverity.MEDIUM);
            alert.setRuleName("MEDIUM_VALUE_PAYMENT");
            alert.setDescription("Payment amount requires finance review");
            return alert;
        }
        alert.setSeverity(FraudSeverity.LOW);
        alert.setStatus(FraudAlertStatus.REVIEWED);
        alert.setRuleName("NO_RULE_MATCHED");
        alert.setDescription("No suspicious rule matched for this payment");
        return alert;
    }
}
