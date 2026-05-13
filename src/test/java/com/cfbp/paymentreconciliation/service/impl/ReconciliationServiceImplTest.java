package com.cfbp.paymentreconciliation.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cfbp.paymentreconciliation.dto.ReconciliationResponse;
import com.cfbp.paymentreconciliation.dto.ReconciliationRunRequest;
import com.cfbp.paymentreconciliation.dto.SettlementRecordRequest;
import com.cfbp.paymentreconciliation.entity.PaymentTransaction;
import com.cfbp.paymentreconciliation.entity.ReconciliationRecord;
import com.cfbp.paymentreconciliation.entity.SettlementRecord;
import com.cfbp.paymentreconciliation.enums.ReconciliationStatus;
import com.cfbp.paymentreconciliation.mapper.ReconciliationMapper;
import com.cfbp.paymentreconciliation.repository.PaymentTransactionRepository;
import com.cfbp.paymentreconciliation.repository.ReconciliationRecordRepository;
import com.cfbp.paymentreconciliation.repository.SettlementRecordRepository;
import com.cfbp.paymentreconciliation.service.AuditService;
import com.cfbp.paymentreconciliation.service.EventPublisher;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReconciliationServiceImplTest {

    @Mock
    private PaymentTransactionRepository paymentRepository;
    @Mock
    private SettlementRecordRepository settlementRepository;
    @Mock
    private ReconciliationRecordRepository reconciliationRepository;
    @Mock
    private ReconciliationMapper reconciliationMapper;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private AuditService auditService;

    private ReconciliationServiceImpl reconciliationService;

    @BeforeEach
    void setUp() {
        reconciliationService = new ReconciliationServiceImpl(paymentRepository, settlementRepository,
                reconciliationRepository, reconciliationMapper, eventPublisher, auditService,
                "reconciliation-completed");
    }

    @Test
    void runReconciliationMarksMatchingPaymentAsMatched() {
        SettlementRecordRequest settlement = new SettlementRecordRequest("SET-001", "PAY-001",
                new BigDecimal("1000.00"), "SUCCESS", LocalDate.now());
        ReconciliationRunRequest request = new ReconciliationRunRequest(List.of(settlement));
        PaymentTransaction payment = payment();
        ReconciliationResponse mapped = new ReconciliationResponse(null, "PAY-001", "SET-001",
                ReconciliationStatus.MATCHED, BigDecimal.ZERO, "matched", null);

        when(settlementRepository.save(any(SettlementRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(paymentRepository.findByPaymentReference("PAY-001")).thenReturn(Optional.of(payment));
        when(reconciliationRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(reconciliationMapper.toResponse(any(ReconciliationRecord.class))).thenReturn(mapped);

        List<ReconciliationResponse> responses = reconciliationService.runReconciliation(request);

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).status()).isEqualTo(ReconciliationStatus.MATCHED);
        verify(eventPublisher).publish("reconciliation-completed", responses);
    }

    private PaymentTransaction payment() {
        PaymentTransaction payment = new PaymentTransaction();
        payment.setPaymentReference("PAY-001");
        payment.setCustomerName("Aarav");
        payment.setSourceAccount("ACC-1");
        payment.setDestinationAccount("ACC-2");
        payment.setAmount(new BigDecimal("1000.00"));
        payment.setCurrency("INR");
        payment.setPaymentMode("UPI");
        return payment;
    }
}
