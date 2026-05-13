package com.cfbp.paymentreconciliation.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cfbp.paymentreconciliation.dto.FraudAlertResponse;
import com.cfbp.paymentreconciliation.entity.FraudAlert;
import com.cfbp.paymentreconciliation.entity.PaymentTransaction;
import com.cfbp.paymentreconciliation.enums.FraudAlertStatus;
import com.cfbp.paymentreconciliation.enums.FraudSeverity;
import com.cfbp.paymentreconciliation.mapper.FraudAlertMapper;
import com.cfbp.paymentreconciliation.repository.FraudAlertRepository;
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
class FraudServiceImplTest {

    @Mock
    private PaymentTransactionRepository paymentRepository;
    @Mock
    private FraudAlertRepository fraudAlertRepository;
    @Mock
    private FraudAlertMapper fraudAlertMapper;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private AuditService auditService;

    private FraudServiceImpl fraudService;

    @BeforeEach
    void setUp() {
        fraudService = new FraudServiceImpl(paymentRepository, fraudAlertRepository, fraudAlertMapper,
                eventPublisher, auditService, "fraud-alert");
    }

    @Test
    void checkPaymentCreatesHighSeverityAlertForLargePayment() {
        PaymentTransaction payment = payment(new BigDecimal("150000"));
        FraudAlertResponse mapped = new FraudAlertResponse(null, null, "PAY-001",
                FraudSeverity.HIGH, FraudAlertStatus.OPEN, "HIGH_VALUE_PAYMENT",
                "Payment amount exceeds high-value fraud threshold", null);

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(fraudAlertRepository.save(any(FraudAlert.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(fraudAlertMapper.toResponse(any(FraudAlert.class))).thenReturn(mapped);

        FraudAlertResponse response = fraudService.checkPayment(1L);

        assertThat(response.severity()).isEqualTo(FraudSeverity.HIGH);
        verify(paymentRepository).save(payment);
        verify(eventPublisher).publish("fraud-alert", mapped);
    }

    private PaymentTransaction payment(BigDecimal amount) {
        PaymentTransaction payment = new PaymentTransaction();
        payment.setPaymentReference("PAY-001");
        payment.setCustomerName("Aarav");
        payment.setSourceAccount("ACC-1");
        payment.setDestinationAccount("ACC-2");
        payment.setAmount(amount);
        payment.setCurrency("INR");
        payment.setPaymentMode("UPI");
        return payment;
    }
}
