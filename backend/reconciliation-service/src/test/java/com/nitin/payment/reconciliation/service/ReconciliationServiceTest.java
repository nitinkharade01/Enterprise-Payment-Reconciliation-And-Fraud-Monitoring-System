package com.nitin.payment.reconciliation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nitin.payment.reconciliation.messaging.ReconciliationEventPublisher;
import com.nitin.payment.reconciliation.repository.ReconciliationRecordRepository;
import com.nitin.payment.reconciliation.repository.ReconciliationSummaryRepository;
import com.nitin.payment.reconciliation.repository.SettlementFileRepository;
import com.nitin.payment.reconciliation.testdata.TestReconciliationFileFactory;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReconciliationServiceTest {

    @Mock
    private SettlementFileRepository fileRepository;
    @Mock
    private ReconciliationRecordRepository recordRepository;
    @Mock
    private ReconciliationSummaryRepository summaryRepository;
    @Mock
    private ReconciliationEventPublisher eventPublisher;

    @Test
    void uploadParsesCsvAndPublishesCompletion() {
        ReconciliationService service = new ReconciliationService(fileRepository, recordRepository, summaryRepository, eventPublisher);
        when(fileRepository.save(any())).thenReturn(TestReconciliationFileFactory.settlementFile());
        when(recordRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(summaryRepository.save(any())).thenReturn(TestReconciliationFileFactory.summary());

        var summary = service.upload(TestReconciliationFileFactory.csvFile());

        assertThat(summary.getFileId()).isEqualTo(10L);
        verify(eventPublisher).publishCompleted(any(), any());
    }
}
