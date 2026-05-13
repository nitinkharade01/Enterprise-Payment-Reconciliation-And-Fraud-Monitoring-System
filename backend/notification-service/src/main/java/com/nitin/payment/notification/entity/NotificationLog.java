/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.nitin.payment.common.BaseEntity
 *  jakarta.persistence.Column
 *  jakarta.persistence.Entity
 *  jakarta.persistence.GeneratedValue
 *  jakarta.persistence.GenerationType
 *  jakarta.persistence.Id
 *  jakarta.persistence.Table
 *  lombok.Generated
 */
package com.nitin.payment.notification.entity;

import com.nitin.payment.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Generated;

@Entity
@Table(name="notifications")
public class NotificationLog
extends BaseEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String recipient;
    private String type;
    private String subject;
    @Column(length=1000)
    private String message;
    private boolean read;

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getRecipient() {
        return this.recipient;
    }

    @Generated
    public String getType() {
        return this.type;
    }

    @Generated
    public String getSubject() {
        return this.subject;
    }

    @Generated
    public String getMessage() {
        return this.message;
    }

    @Generated
    public boolean isRead() {
        return this.read;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    @Generated
    public void setType(String type) {
        this.type = type;
    }

    @Generated
    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Generated
    public void setMessage(String message) {
        this.message = message;
    }

    @Generated
    public void setRead(boolean read) {
        this.read = read;
    }
}
