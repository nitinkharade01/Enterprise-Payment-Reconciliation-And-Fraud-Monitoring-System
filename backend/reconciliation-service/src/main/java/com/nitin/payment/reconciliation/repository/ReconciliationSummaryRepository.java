/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.data.jpa.repository.JpaRepository
 */
package com.nitin.payment.reconciliation.repository;

import com.nitin.payment.reconciliation.entity.ReconciliationSummary;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReconciliationSummaryRepository
extends JpaRepository<ReconciliationSummary, Long> {
    public Optional<ReconciliationSummary> findByFileId(Long var1);
}
