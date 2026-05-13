/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.stereotype.Component
 */
package com.nitin.payment.transaction.service;

import com.nitin.payment.transaction.dto.TransactionResponse;
import com.nitin.payment.transaction.entity.PaymentTransaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    public TransactionResponse toResponse(PaymentTransaction tx) {
        return new TransactionResponse(tx.getId(), tx.getTransactionId(), tx.getUserId(), tx.getCustomerName(), tx.getSourceAccount(), tx.getDestinationAccount(), tx.getAmount(), tx.getCurrency(), tx.getPaymentMode(), tx.getTransactionStatus(), tx.getRiskStatus(), tx.getReconciliationStatus(), tx.getRemarks(), tx.getCreatedAt(), tx.getUpdatedAt());
    }
}
