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
package com.nitin.payment.transaction.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Generated;

@Entity
@Table(name="transaction_audit_logs")
public class TransactionAuditLog {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String transactionId;
    private String action;
    private String oldValue;
    private String newValue;
    private String changedBy;
    private Instant createdAt = Instant.now();

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getTransactionId() {
        return this.transactionId;
    }

    @Generated
    public String getAction() {
        return this.action;
    }

    @Generated
    public String getOldValue() {
        return this.oldValue;
    }

    @Generated
    public String getNewValue() {
        return this.newValue;
    }

    @Generated
    public String getChangedBy() {
        return this.changedBy;
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
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Generated
    public void setAction(String action) {
        this.action = action;
    }

    @Generated
    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    @Generated
    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    @Generated
    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    @Generated
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
