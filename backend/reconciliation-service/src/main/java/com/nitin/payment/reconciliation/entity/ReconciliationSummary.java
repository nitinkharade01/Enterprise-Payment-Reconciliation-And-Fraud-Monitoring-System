/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  jakarta.persistence.Column
 *  jakarta.persistence.Entity
 *  jakarta.persistence.GeneratedValue
 *  jakarta.persistence.GenerationType
 *  jakarta.persistence.Id
 *  jakarta.persistence.Table
 *  lombok.Generated
 */
package com.nitin.payment.reconciliation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Generated;

@Entity
@Table(name="reconciliation_summary")
public class ReconciliationSummary {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(unique=true)
    private Long fileId;
    private long totalRecords;
    private long matchedCount;
    private long mismatchedCount;
    private long missingCount;
    private long duplicateCount;

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public Long getFileId() {
        return this.fileId;
    }

    @Generated
    public long getTotalRecords() {
        return this.totalRecords;
    }

    @Generated
    public long getMatchedCount() {
        return this.matchedCount;
    }

    @Generated
    public long getMismatchedCount() {
        return this.mismatchedCount;
    }

    @Generated
    public long getMissingCount() {
        return this.missingCount;
    }

    @Generated
    public long getDuplicateCount() {
        return this.duplicateCount;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    @Generated
    public void setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
    }

    @Generated
    public void setMatchedCount(long matchedCount) {
        this.matchedCount = matchedCount;
    }

    @Generated
    public void setMismatchedCount(long mismatchedCount) {
        this.mismatchedCount = mismatchedCount;
    }

    @Generated
    public void setMissingCount(long missingCount) {
        this.missingCount = missingCount;
    }

    @Generated
    public void setDuplicateCount(long duplicateCount) {
        this.duplicateCount = duplicateCount;
    }
}
