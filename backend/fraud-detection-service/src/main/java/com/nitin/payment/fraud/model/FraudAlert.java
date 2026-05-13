/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.index.Indexed
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.nitin.payment.fraud.model;

import java.time.Instant;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value="fraud_alerts")
public class FraudAlert {
    @Id
    private String id;
    @Indexed
    private String transactionId;
    private Long userId;
    private int riskScore;
    private String riskStatus;
    private String fraudReason;
    private String alertStatus = "OPEN";
    private Instant createdAt = Instant.now();

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getTransactionId() {
        return this.transactionId;
    }

    @Generated
    public Long getUserId() {
        return this.userId;
    }

    @Generated
    public int getRiskScore() {
        return this.riskScore;
    }

    @Generated
    public String getRiskStatus() {
        return this.riskStatus;
    }

    @Generated
    public String getFraudReason() {
        return this.fraudReason;
    }

    @Generated
    public String getAlertStatus() {
        return this.alertStatus;
    }

    @Generated
    public Instant getCreatedAt() {
        return this.createdAt;
    }

    @Generated
    public void setId(String id) {
        this.id = id;
    }

    @Generated
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Generated
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Generated
    public void setRiskScore(int riskScore) {
        this.riskScore = riskScore;
    }

    @Generated
    public void setRiskStatus(String riskStatus) {
        this.riskStatus = riskStatus;
    }

    @Generated
    public void setFraudReason(String fraudReason) {
        this.fraudReason = fraudReason;
    }

    @Generated
    public void setAlertStatus(String alertStatus) {
        this.alertStatus = alertStatus;
    }

    @Generated
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
