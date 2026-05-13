package com.cfbp.paymentreconciliation.service.impl;

import com.cfbp.paymentreconciliation.dto.ReconciliationResponse;
import com.cfbp.paymentreconciliation.dto.ReconciliationRunRequest;
import com.cfbp.paymentreconciliation.dto.SettlementRecordRequest;
import com.cfbp.paymentreconciliation.entity.PaymentTransaction;
import com.cfbp.paymentreconciliation.entity.ReconciliationRecord;
import com.cfbp.paymentreconciliation.entity.SettlementRecord;
import com.cfbp.paymentreconciliation.enums.AuditAction;
import com.cfbp.paymentreconciliation.enums.ReconciliationStatus;
import com.cfbp.paymentreconciliation.exception.ResourceNotFoundException;
import com.cfbp.paymentreconciliation.mapper.ReconciliationMapper;
import com.cfbp.paymentreconciliation.repository.PaymentTransactionRepository;
import com.cfbp.paymentreconciliation.repository.ReconciliationRecordRepository;
import com.cfbp.paymentreconciliation.repository.SettlementRecordRepository;
import com.cfbp.paymentreconciliation.service.AuditService;
import com.cfbp.paymentreconciliation.service.EventPublisher;
import com.cfbp.paymentreconciliation.service.ReconciliationService;
import com.cfbp.paymentreconciliation.util.CurrentUserUtil;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReconciliationServiceImpl implements ReconciliationService {

    private final PaymentTransactionRepository paymentRepository;
    private final SettlementRecordRepository settlementRepository;
    private final ReconciliationRecordRepository reconciliationRepository;
    private final ReconciliationMapper reconciliationMapper;
    private final EventPublisher eventPublisher;
    private final AuditService auditService;
    private final String reconciliationCompletedTopic;

    public ReconciliationServiceImpl(PaymentTransactionRepository paymentRepository,
                                     SettlementRecordRepository settlementRepository,
                                     ReconciliationRecordRepository reconciliationRepository,
                                     ReconciliationMapper reconciliationMapper,
                                     EventPublisher eventPublisher,
                                     AuditService auditService,
                                     @Value("${app.kafka.topics.reconciliation-completed}") String reconciliationCompletedTopic) {
        this.paymentRepository = paymentRepository;
        this.settlementRepository = settlementRepository;
        this.reconciliationRepository = reconciliationRepository;
        this.reconciliationMapper = reconciliationMapper;
        this.eventPublisher = eventPublisher;
        this.auditService = auditService;
        this.reconciliationCompletedTopic = reconciliationCompletedTopic;
    }

    @Override
    @Transactional
    public List<ReconciliationResponse> runReconciliation(ReconciliationRunRequest request) {
        List<ReconciliationRecord> records = request.settlementRecords().stream()
                .map(this::reconcileOne)
                .toList();
        List<ReconciliationRecord> saved = reconciliationRepository.saveAll(records);
        List<ReconciliationResponse> response = saved.stream().map(reconciliationMapper::toResponse).toList();
        eventPublisher.publish(reconciliationCompletedTopic, response);
        auditService.log(CurrentUserUtil.username(), AuditAction.RECONCILIATION_COMPLETED, "ReconciliationRecord",
                "batch", "Reconciliation completed for " + response.size() + " settlement records");
        return response;
    }

    @Override
    @Cacheable(cacheNames = "reconciliations", key = "#id")
    public ReconciliationResponse getReconciliation(Long id) {
        ReconciliationRecord record = reconciliationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reconciliation record not found with id " + id));
        return reconciliationMapper.toResponse(record);
    }

    @Override
    public List<ReconciliationResponse> getReconciliations() {
        return reconciliationRepository.findAll().stream()
                .sorted(Comparator.comparing(ReconciliationRecord::getReconciledAt).reversed())
                .map(reconciliationMapper::toResponse)
                .toList();
    }

    private ReconciliationRecord reconcileOne(SettlementRecordRequest request) {
        SettlementRecord settlement = new SettlementRecord();
        settlement.setSettlementReference(request.settlementReference());
        settlement.setPaymentReference(request.paymentReference());
        settlement.setAmount(request.amount());
        settlement.setSettlementStatus(request.settlementStatus());
        settlement.setSettlementDate(request.settlementDate());
        SettlementRecord savedSettlement = settlementRepository.save(settlement);

        ReconciliationRecord record = new ReconciliationRecord();
        record.setSettlementRecord(savedSettlement);

        PaymentTransaction payment = paymentRepository.findByPaymentReference(request.paymentReference()).orElse(null);
        if (payment == null) {
            record.setStatus(ReconciliationStatus.MISSING_INTERNAL);
            record.setRemarks("Settlement record has no matching internal payment");
            record.setDifferenceAmount(request.amount());
            return record;
        }

        record.setPaymentTransaction(payment);
        BigDecimal difference = payment.getAmount().subtract(request.amount());
        record.setDifferenceAmount(difference.abs());
        if (difference.compareTo(BigDecimal.ZERO) == 0) {
            record.setStatus(ReconciliationStatus.MATCHED);
            record.setRemarks("Internal payment and settlement amount matched");
        } else {
            record.setStatus(ReconciliationStatus.MISMATCHED);
            record.setRemarks("Amount mismatch between internal payment and settlement record");
        }
        return record;
    }
}
