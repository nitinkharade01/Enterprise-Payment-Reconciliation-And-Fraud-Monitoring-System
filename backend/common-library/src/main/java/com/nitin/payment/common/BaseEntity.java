/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  jakarta.persistence.Column
 *  jakarta.persistence.MappedSuperclass
 *  jakarta.persistence.PrePersist
 *  jakarta.persistence.PreUpdate
 *  lombok.Generated
 */
package com.nitin.payment.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.Instant;
import lombok.Generated;

@MappedSuperclass
public abstract class BaseEntity {
    @Column(nullable=false, updatable=false)
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @PrePersist
    void prePersist() {
        this.updatedAt = this.createdAt = Instant.now();
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = Instant.now();
    }

    @Generated
    public Instant getCreatedAt() {
        return this.createdAt;
    }

    @Generated
    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    @Generated
    public String getCreatedBy() {
        return this.createdBy;
    }

    @Generated
    public String getUpdatedBy() {
        return this.updatedBy;
    }

    @Generated
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Generated
    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Generated
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Generated
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
