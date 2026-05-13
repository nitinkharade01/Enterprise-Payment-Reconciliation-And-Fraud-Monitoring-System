/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.nitin.payment.common.exception.ResourceNotFoundException
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.data.domain.Page
 *  org.springframework.data.domain.Pageable
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 */
package com.nitin.payment.transaction.service;

import com.nitin.payment.common.exception.ResourceNotFoundException;
import com.nitin.payment.transaction.dto.CreateTransactionRequest;
import com.nitin.payment.transaction.dto.TransactionResponse;
import com.nitin.payment.transaction.dto.UpdateStatusRequest;
import com.nitin.payment.transaction.entity.PaymentTransaction;
import com.nitin.payment.transaction.entity.ReconciliationStatus;
import com.nitin.payment.transaction.entity.RiskStatus;
import com.nitin.payment.transaction.entity.TransactionAuditLog;
import com.nitin.payment.transaction.entity.TransactionStatus;
import com.nitin.payment.transaction.messaging.TransactionEventPublisher;
import com.nitin.payment.transaction.repository.TransactionAuditLogRepository;
import com.nitin.payment.transaction.repository.TransactionRepository;
import com.nitin.payment.transaction.service.TransactionMapper;
import java.util.UUID;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);
    private final TransactionRepository repository;
    private final TransactionAuditLogRepository auditRepository;
    private final TransactionMapper mapper;
    private final TransactionEventPublisher eventPublisher;

    @Transactional
    public TransactionResponse create(CreateTransactionRequest request) {
        PaymentTransaction tx = new PaymentTransaction();
        tx.setTransactionId("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        tx.setUserId(request.userId());
        tx.setCustomerName(request.customerName());
        tx.setSourceAccount(request.sourceAccount());
        tx.setDestinationAccount(request.destinationAccount());
        tx.setAmount(request.amount());
        tx.setCurrency(request.currency());
        tx.setPaymentMode(request.paymentMode());
        tx.setTransactionStatus(TransactionStatus.INITIATED);
        tx.setRiskStatus(RiskStatus.LOW_RISK);
        tx.setReconciliationStatus(ReconciliationStatus.PENDING);
        tx.setRemarks(request.remarks());
        PaymentTransaction saved = this.repository.save(tx);
        this.eventPublisher.publishCreated(saved);
        return this.mapper.toResponse(saved);
    }

    public Page<TransactionResponse> findAll(Pageable pageable) {
        return this.repository.findAll(pageable).map(this.mapper::toResponse);
    }

    public TransactionResponse findByTransactionId(String transactionId) {
        return this.mapper.toResponse(this.load(transactionId));
    }

    public Page<TransactionResponse> byStatus(TransactionStatus status, Pageable pageable) {
        return this.repository.findByTransactionStatus(status, pageable).map(this.mapper::toResponse);
    }

    public Page<TransactionResponse> search(String keyword, Pageable pageable) {
        return this.repository.findByCustomerNameContainingIgnoreCaseOrTransactionIdContainingIgnoreCase(keyword, keyword, pageable).map(this.mapper::toResponse);
    }

    @Transactional
    public TransactionResponse updateStatus(String transactionId, UpdateStatusRequest request) {
        PaymentTransaction tx = this.load(transactionId);
        TransactionStatus old = tx.getTransactionStatus();
        tx.setTransactionStatus(request.status());
        tx.setRemarks(request.remarks());
        TransactionAuditLog logEntry = new TransactionAuditLog();
        logEntry.setTransactionId(transactionId);
        logEntry.setAction("STATUS_CHANGED");
        logEntry.setOldValue(old.name());
        logEntry.setNewValue(request.status().name());
        this.auditRepository.save(logEntry);
        this.eventPublisher.publishStatusUpdated(tx);
        return this.mapper.toResponse(tx);
    }

    private PaymentTransaction load(String transactionId) {
        return this.repository.findByTransactionId(transactionId).orElseThrow(() -> new ResourceNotFoundException("Transaction not found: " + transactionId));
    }

    @Generated
    public TransactionService(TransactionRepository repository, TransactionAuditLogRepository auditRepository, TransactionMapper mapper, TransactionEventPublisher eventPublisher) {
        this.repository = repository;
        this.auditRepository = auditRepository;
        this.mapper = mapper;
        this.eventPublisher = eventPublisher;
    }
}
