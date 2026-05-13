/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.nitin.payment.common.BaseEntity
 *  jakarta.persistence.Column
 *  jakarta.persistence.Entity
 *  jakarta.persistence.EnumType
 *  jakarta.persistence.Enumerated
 *  jakarta.persistence.GeneratedValue
 *  jakarta.persistence.GenerationType
 *  jakarta.persistence.Id
 *  jakarta.persistence.Index
 *  jakarta.persistence.Table
 *  lombok.Generated
 */
package com.nitin.payment.transaction.entity;

import com.nitin.payment.common.BaseEntity;
import com.nitin.payment.transaction.entity.ReconciliationStatus;
import com.nitin.payment.transaction.entity.RiskStatus;
import com.nitin.payment.transaction.entity.TransactionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Generated;

@Entity
@Table(name="transactions", indexes={@Index(name="idx_transactions_transaction_id", columnList="transactionId"), @Index(name="idx_transactions_user_status", columnList="userId, transactionStatus")})
public class PaymentTransaction
extends BaseEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, unique=true)
    private String transactionId;
    @Column(nullable=false)
    private Long userId;
    @Column(nullable=false)
    private String customerName;
    @Column(nullable=false)
    private String sourceAccount;
    @Column(nullable=false)
    private String destinationAccount;
    @Column(nullable=false, precision=18, scale=2)
    private BigDecimal amount;
    @Column(nullable=false)
    private String currency;
    @Column(nullable=false)
    private String paymentMode;
    @Enumerated(value=EnumType.STRING)
    private TransactionStatus transactionStatus;
    @Enumerated(value=EnumType.STRING)
    private RiskStatus riskStatus;
    @Enumerated(value=EnumType.STRING)
    private ReconciliationStatus reconciliationStatus;
    private String remarks;

    @Generated
    public Long getId() {
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
    public String getCustomerName() {
        return this.customerName;
    }

    @Generated
    public String getSourceAccount() {
        return this.sourceAccount;
    }

    @Generated
    public String getDestinationAccount() {
        return this.destinationAccount;
    }

    @Generated
    public BigDecimal getAmount() {
        return this.amount;
    }

    @Generated
    public String getCurrency() {
        return this.currency;
    }

    @Generated
    public String getPaymentMode() {
        return this.paymentMode;
    }

    @Generated
    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }

    @Generated
    public RiskStatus getRiskStatus() {
        return this.riskStatus;
    }

    @Generated
    public ReconciliationStatus getReconciliationStatus() {
        return this.reconciliationStatus;
    }

    @Generated
    public String getRemarks() {
        return this.remarks;
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
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Generated
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Generated
    public void setSourceAccount(String sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    @Generated
    public void setDestinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    @Generated
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Generated
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Generated
    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    @Generated
    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    @Generated
    public void setRiskStatus(RiskStatus riskStatus) {
        this.riskStatus = riskStatus;
    }

    @Generated
    public void setReconciliationStatus(ReconciliationStatus reconciliationStatus) {
        this.reconciliationStatus = reconciliationStatus;
    }

    @Generated
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
