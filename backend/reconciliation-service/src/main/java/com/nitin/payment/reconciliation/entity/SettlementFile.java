/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.nitin.payment.common.BaseEntity
 *  jakarta.persistence.Entity
 *  jakarta.persistence.GeneratedValue
 *  jakarta.persistence.GenerationType
 *  jakarta.persistence.Id
 *  jakarta.persistence.Table
 *  lombok.Generated
 */
package com.nitin.payment.reconciliation.entity;

import com.nitin.payment.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Generated;

@Entity
@Table(name="settlement_files")
public class SettlementFile
extends BaseEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String originalFileName;
    private String fileType;
    private String status;

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getOriginalFileName() {
        return this.originalFileName;
    }

    @Generated
    public String getFileType() {
        return this.fileType;
    }

    @Generated
    public String getStatus() {
        return this.status;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    @Generated
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Generated
    public void setStatus(String status) {
        this.status = status;
    }
}
