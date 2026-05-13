/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.data.jpa.repository.JpaRepository
 */
package com.nitin.payment.transaction.repository;

import com.nitin.payment.transaction.entity.TransactionAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionAuditLogRepository
extends JpaRepository<TransactionAuditLog, Long> {
}
