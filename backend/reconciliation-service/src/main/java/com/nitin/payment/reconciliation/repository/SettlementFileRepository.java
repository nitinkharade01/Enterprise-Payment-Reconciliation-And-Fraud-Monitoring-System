/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.data.jpa.repository.JpaRepository
 */
package com.nitin.payment.reconciliation.repository;

import com.nitin.payment.reconciliation.entity.SettlementFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementFileRepository
extends JpaRepository<SettlementFile, Long> {
}
