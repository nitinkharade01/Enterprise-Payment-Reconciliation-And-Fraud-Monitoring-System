package com.cfbp.paymentreconciliation.service.impl;

import com.cfbp.paymentreconciliation.dto.PaymentRequest;
import com.cfbp.paymentreconciliation.dto.PaymentResponse;
import com.cfbp.paymentreconciliation.dto.PaymentValidationResponse;
import com.cfbp.paymentreconciliation.entity.PaymentTransaction;
import com.cfbp.paymentreconciliation.enums.AuditAction;
import com.cfbp.paymentreconciliation.enums.PaymentStatus;
import com.cfbp.paymentreconciliation.exception.DuplicateResourceException;
import com.cfbp.paymentreconciliation.exception.InvalidRequestException;
import com.cfbp.paymentreconciliation.exception.ResourceNotFoundException;
import com.cfbp.paymentreconciliation.mapper.PaymentMapper;
import com.cfbp.paymentreconciliation.repository.PaymentTransactionRepository;
import com.cfbp.paymentreconciliation.service.AuditService;
import com.cfbp.paymentreconciliation.service.EventPublisher;
import com.cfbp.paymentreconciliation.service.PaymentService;
import com.cfbp.paymentreconciliation.util.CurrentUserUtil;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentTransactionRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final EventPublisher eventPublisher;
    private final AuditService auditService;
    private final String paymentCreatedTopic;
    private final String paymentValidatedTopic;

    public PaymentServiceImpl(PaymentTransactionRepository paymentRepository,
                              PaymentMapper paymentMapper,
                              EventPublisher eventPublisher,
                              AuditService auditService,
                              @Value("${app.kafka.topics.payment-created}") String paymentCreatedTopic,
                              @Value("${app.kafka.topics.payment-validated}") String paymentValidatedTopic) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.eventPublisher = eventPublisher;
        this.auditService = auditService;
        this.paymentCreatedTopic = paymentCreatedTopic;
        this.paymentValidatedTopic = paymentValidatedTopic;
    }

    @Override
    @Transactional
    public PaymentResponse createPayment(PaymentRequest request) {
        PaymentTransaction transaction = paymentMapper.toEntity(request);
        if (paymentRepository.existsByPaymentReference(transaction.getPaymentReference())) {
            throw new DuplicateResourceException("Payment reference already exists");
        }
        validateBasicPayment(transaction);
        PaymentTransaction saved = paymentRepository.save(transaction);
        eventPublisher.publish(paymentCreatedTopic, paymentMapper.toResponse(saved));
        auditService.log(CurrentUserUtil.username(), AuditAction.PAYMENT_CREATED, "PaymentTransaction",
                String.valueOf(saved.getId()), "Payment created");
        return paymentMapper.toResponse(saved);
    }

    @Override
    @Cacheable(cacheNames = "payments", key = "#id")
    public PaymentResponse getPayment(Long id) {
        return paymentMapper.toResponse(findPayment(id));
    }

    @Override
    public List<PaymentResponse> getPayments() {
        return paymentRepository.findAll().stream().map(paymentMapper::toResponse).toList();
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "payments", key = "#id")
    public PaymentValidationResponse validatePayment(Long id) {
        PaymentTransaction transaction = findPayment(id);
        try {
            validateBasicPayment(transaction);
            transaction.setStatus(PaymentStatus.VALIDATED);
            transaction.setValidationMessage("Payment validation completed successfully");
        } catch (InvalidRequestException exception) {
            transaction.setStatus(PaymentStatus.REJECTED);
            transaction.setValidationMessage(exception.getMessage());
        }
        PaymentTransaction saved = paymentRepository.save(transaction);
        eventPublisher.publish(paymentValidatedTopic, paymentMapper.toResponse(saved));
        auditService.log(CurrentUserUtil.username(), AuditAction.PAYMENT_VALIDATED, "PaymentTransaction",
                String.valueOf(saved.getId()), saved.getValidationMessage());
        return new PaymentValidationResponse(saved.getId(), saved.getPaymentReference(), saved.getStatus(), saved.getValidationMessage());
    }

    private PaymentTransaction findPayment(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id " + id));
    }

    private void validateBasicPayment(PaymentTransaction transaction) {
        if (transaction.getAmount() == null || transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidRequestException("Payment amount must be greater than zero");
        }
        if (transaction.getSourceAccount().equalsIgnoreCase(transaction.getDestinationAccount())) {
            throw new InvalidRequestException("Source and destination accounts cannot be same");
        }
        if (transaction.getCurrency().length() != 3) {
            throw new InvalidRequestException("Currency must be a three-letter code");
        }
    }
}
