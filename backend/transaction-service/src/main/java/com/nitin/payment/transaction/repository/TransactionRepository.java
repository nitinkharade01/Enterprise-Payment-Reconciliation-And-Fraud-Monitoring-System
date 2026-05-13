/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.data.domain.Page
 *  org.springframework.data.domain.Pageable
 *  org.springframework.data.jpa.repository.JpaRepository
 */
package com.nitin.payment.transaction.repository;

import com.nitin.payment.transaction.entity.PaymentTransaction;
import com.nitin.payment.transaction.entity.ReconciliationStatus;
import com.nitin.payment.transaction.entity.TransactionStatus;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository
extends JpaRepository<PaymentTransaction, Long> {
    public Optional<PaymentTransaction> findByTransactionId(String var1);

    public Page<PaymentTransaction> findByTransactionStatus(TransactionStatus var1, Pageable var2);

    public Page<PaymentTransaction> findByCustomerNameContainingIgnoreCaseOrTransactionIdContainingIgnoreCase(String var1, String var2, Pageable var3);

    public long countByTransactionStatus(TransactionStatus var1);

    public long countByReconciliationStatus(ReconciliationStatus var1);

    public List<PaymentTransaction> findByUserIdAndCreatedAtAfter(Long var1, Instant var2);
}
