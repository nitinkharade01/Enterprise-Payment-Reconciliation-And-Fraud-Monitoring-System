package com.nitin.payment.transaction.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nitin.payment.transaction.dto.TransactionResponse;
import com.nitin.payment.transaction.messaging.TransactionEventPublisher;
import com.nitin.payment.transaction.repository.TransactionAuditLogRepository;
import com.nitin.payment.transaction.repository.TransactionRepository;
import com.nitin.payment.transaction.testdata.TestTransactionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository repository;
    @Mock
    private TransactionAuditLogRepository auditRepository;
    @Mock
    private TransactionEventPublisher eventPublisher;

    @Test
    void createPersistsAndPublishesTransaction() {
        TransactionMapper mapper = new TransactionMapper();
        TransactionService service = new TransactionService(repository, auditRepository, mapper, eventPublisher);
        when(repository.save(any())).thenReturn(TestTransactionFactory.transaction());

        TransactionResponse response = service.create(TestTransactionFactory.createRequest());

        assertThat(response.transactionId()).isEqualTo("TXN-TEST-001");
        verify(eventPublisher).publishCreated(any());
    }
}
