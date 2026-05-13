/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  jakarta.persistence.Entity
 *  jakarta.persistence.GeneratedValue
 *  jakarta.persistence.GenerationType
 *  jakarta.persistence.Id
 *  jakarta.persistence.Table
 *  lombok.Generated
 */
package com.nitin.payment.reconciliation.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Generated;

@Entity
@Table(name="reconciliation_records")
public class ReconciliationRecord {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String internalTransactionId;
    private String bankTransactionId;
    private BigDecimal internalAmount;
    private BigDecimal bankAmount;
    private String internalStatus;
    private String bankStatus;
    private String reconciliationStatus;
    private String mismatchReason;
    private Long fileId;
    private Instant createdAt = Instant.now();

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getInternalTransactionId() {
        return this.internalTransactionId;
    }

    @Generated
    public String getBankTransactionId() {
        return this.bankTransactionId;
    }

    @Generated
    public BigDecimal getInternalAmount() {
        return this.internalAmount;
    }

    @Generated
    public BigDecimal getBankAmount() {
        return this.bankAmount;
    }

    @Generated
    public String getInternalStatus() {
        return this.internalStatus;
    }

    @Generated
    public String getBankStatus() {
        return this.bankStatus;
    }

    @Generated
    public String getReconciliationStatus() {
        return this.reconciliationStatus;
    }

    @Generated
    public String getMismatchReason() {
        return this.mismatchReason;
    }

    @Generated
    public Long getFileId() {
        return this.fileId;
    }

    @Generated
    public Instant getCreatedAt() {
        return this.createdAt;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setInternalTransactionId(String internalTransactionId) {
        this.internalTransactionId = internalTransactionId;
    }

    @Generated
    public void setBankTransactionId(String bankTransactionId) {
        this.bankTransactionId = bankTransactionId;
    }

    @Generated
    public void setInternalAmount(BigDecimal internalAmount) {
        this.internalAmount = internalAmount;
    }

    @Generated
    public void setBankAmount(BigDecimal bankAmount) {
        this.bankAmount = bankAmount;
    }

    @Generated
    public void setInternalStatus(String internalStatus) {
        this.internalStatus = internalStatus;
    }

    @Generated
    public void setBankStatus(String bankStatus) {
        this.bankStatus = bankStatus;
    }

    @Generated
    public void setReconciliationStatus(String reconciliationStatus) {
        this.reconciliationStatus = reconciliationStatus;
    }

    @Generated
    public void setMismatchReason(String mismatchReason) {
        this.mismatchReason = mismatchReason;
    }

    @Generated
    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    @Generated
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
