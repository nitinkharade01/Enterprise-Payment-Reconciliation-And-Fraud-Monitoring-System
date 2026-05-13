package com.cfbp.paymentreconciliation.mapper;

import com.cfbp.paymentreconciliation.dto.PaymentRequest;
import com.cfbp.paymentreconciliation.dto.PaymentResponse;
import com.cfbp.paymentreconciliation.entity.PaymentTransaction;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentTransaction toEntity(PaymentRequest request) {
        PaymentTransaction transaction = new PaymentTransaction();
        transaction.setPaymentReference(resolveReference(request.paymentReference()));
        transaction.setCustomerName(request.customerName());
        transaction.setSourceAccount(request.sourceAccount());
        transaction.setDestinationAccount(request.destinationAccount());
        transaction.setAmount(request.amount());
        transaction.setCurrency(request.currency().toUpperCase());
        transaction.setPaymentMode(request.paymentMode().toUpperCase());
        return transaction;
    }

    public PaymentResponse toResponse(PaymentTransaction transaction) {
        return new PaymentResponse(
                transaction.getId(),
                transaction.getPaymentReference(),
                transaction.getCustomerName(),
                transaction.getSourceAccount(),
                transaction.getDestinationAccount(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getPaymentMode(),
                transaction.getStatus(),
                transaction.getValidationMessage(),
                transaction.isFraudFlagged(),
                transaction.getCreatedAt(),
                transaction.getUpdatedAt()
        );
    }

    private String resolveReference(String paymentReference) {
        if (paymentReference == null || paymentReference.isBlank()) {
            return "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
        return paymentReference.trim();
    }
}
