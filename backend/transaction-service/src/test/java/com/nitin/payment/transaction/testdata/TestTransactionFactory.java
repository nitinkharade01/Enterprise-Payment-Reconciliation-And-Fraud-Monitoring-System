package com.nitin.payment.transaction.testdata;

import com.nitin.payment.transaction.dto.CreateTransactionRequest;
import com.nitin.payment.transaction.dto.TransactionResponse;
import com.nitin.payment.transaction.entity.PaymentTransaction;
import com.nitin.payment.transaction.entity.ReconciliationStatus;
import com.nitin.payment.transaction.entity.RiskStatus;
import com.nitin.payment.transaction.entity.TransactionStatus;
import java.math.BigDecimal;

public final class TestTransactionFactory {

    private TestTransactionFactory() {
    }

    public static CreateTransactionRequest createRequest() {
        return new CreateTransactionRequest(2L, "Aarav Mehta", "ACC-1001", "ACC-9001", new BigDecimal("76000"), "INR", "UPI", "Invoice payment");
    }

    public static PaymentTransaction transaction() {
        PaymentTransaction tx = new PaymentTransaction();
        tx.setId(1L);
        tx.setTransactionId("TXN-TEST-001");
        tx.setUserId(2L);
        tx.setCustomerName("Aarav Mehta");
        tx.setSourceAccount("ACC-1001");
        tx.setDestinationAccount("ACC-9001");
        tx.setAmount(new BigDecimal("76000"));
        tx.setCurrency("INR");
        tx.setPaymentMode("UPI");
        tx.setTransactionStatus(TransactionStatus.INITIATED);
        tx.setRiskStatus(RiskStatus.LOW_RISK);
        tx.setReconciliationStatus(ReconciliationStatus.PENDING);
        tx.setRemarks("Invoice payment");
        return tx;
    }

    public static TransactionResponse response() {
        PaymentTransaction tx = transaction();
        return new TransactionResponse(tx.getId(), tx.getTransactionId(), tx.getUserId(), tx.getCustomerName(), tx.getSourceAccount(), tx.getDestinationAccount(), tx.getAmount(), tx.getCurrency(), tx.getPaymentMode(), tx.getTransactionStatus(), tx.getRiskStatus(), tx.getReconciliationStatus(), tx.getRemarks(), tx.getCreatedAt(), tx.getUpdatedAt());
    }
}
