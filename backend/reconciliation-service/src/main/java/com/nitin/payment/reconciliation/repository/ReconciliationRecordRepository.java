/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.data.jpa.repository.JpaRepository
 */
package com.nitin.payment.reconciliation.repository;

import com.nitin.payment.reconciliation.entity.ReconciliationRecord;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReconciliationRecordRepository
extends JpaRepository<ReconciliationRecord, Long> {
    public List<ReconciliationRecord> findByFileId(Long var1);

    public List<ReconciliationRecord> findByFileIdAndReconciliationStatusNot(Long var1, String var2);
}
