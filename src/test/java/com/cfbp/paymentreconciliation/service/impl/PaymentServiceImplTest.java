package com.cfbp.paymentreconciliation.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cfbp.paymentreconciliation.dto.PaymentRequest;
import com.cfbp.paymentreconciliation.dto.PaymentResponse;
import com.cfbp.paymentreconciliation.dto.PaymentValidationResponse;
import com.cfbp.paymentreconciliation.entity.PaymentTransaction;
import com.cfbp.paymentreconciliation.enums.PaymentStatus;
import com.cfbp.paymentreconciliation.exception.ResourceNotFoundException;
import com.cfbp.paymentreconciliation.mapper.PaymentMapper;
import com.cfbp.paymentreconciliation.repository.PaymentTransactionRepository;
import com.cfbp.paymentreconciliation.service.AuditService;
import com.cfbp.paymentreconciliation.service.EventPublisher;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentTransactionRepository paymentRepository;
    @Mock
    private PaymentMapper paymentMapper;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private AuditService auditService;

    private PaymentServiceImpl paymentService;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentServiceImpl(
                paymentRepository, paymentMapper, eventPublisher, auditService, "payment-created", "payment-validated"
        );
    }

    @Test
    void createPaymentStoresPaymentAndPublishesEvent() {
        PaymentRequest request = request();
        PaymentTransaction transaction = transaction();
        PaymentResponse response = response(PaymentStatus.CREATED);
        when(paymentMapper.toEntity(request)).thenReturn(transaction);
        when(paymentRepository.existsByPaymentReference("PAY-001")).thenReturn(false);
        when(paymentRepository.save(transaction)).thenReturn(transaction);
        when(paymentMapper.toResponse(transaction)).thenReturn(response);

        PaymentResponse result = paymentService.createPayment(request);

        assertThat(result.paymentReference()).isEqualTo("PAY-001");
        verify(eventPublisher).publish("payment-created", response);
        verify(auditService).log(any(), any(), any(), any(), any());
    }

    @Test
    void validatePaymentRejectsMissingPayment() {
        when(paymentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.validatePayment(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void validatePaymentMarksValidPaymentAsValidated() {
        PaymentTransaction transaction = transaction();
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(paymentRepository.save(transaction)).thenReturn(transaction);

        PaymentValidationResponse response = paymentService.validatePayment(1L);

        assertThat(response.status()).isEqualTo(PaymentStatus.VALIDATED);
        verify(eventPublisher).publish("payment-validated", null);
    }

    private PaymentRequest request() {
        return new PaymentRequest("PAY-001", "Aarav", "ACC-1", "ACC-2",
                new BigDecimal("500.00"), "INR", "UPI");
    }

    private PaymentTransaction transaction() {
        PaymentTransaction transaction = new PaymentTransaction();
        transaction.setPaymentReference("PAY-001");
        transaction.setCustomerName("Aarav");
        transaction.setSourceAccount("ACC-1");
        transaction.setDestinationAccount("ACC-2");
        transaction.setAmount(new BigDecimal("500.00"));
        transaction.setCurrency("INR");
        transaction.setPaymentMode("UPI");
        return transaction;
    }

    private PaymentResponse response(PaymentStatus status) {
        return new PaymentResponse(null, "PAY-001", "Aarav", "ACC-1", "ACC-2",
                new BigDecimal("500.00"), "INR", "UPI", status, null, false, null, null);
    }
}
