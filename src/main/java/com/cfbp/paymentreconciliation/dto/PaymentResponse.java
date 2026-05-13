package com.cfbp.paymentreconciliation.dto;

import com.cfbp.paymentreconciliation.enums.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        String paymentReference,
        String customerName,
        String sourceAccount,
        String destinationAccount,
        BigDecimal amount,
        String currency,
        String paymentMode,
        PaymentStatus status,
        String validationMessage,
        boolean fraudFlagged,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
