package com.cfbp.paymentreconciliation.dto;

import com.cfbp.paymentreconciliation.enums.PaymentStatus;

public record PaymentValidationResponse(
        Long paymentId,
        String paymentReference,
        PaymentStatus status,
        String message
) {
}
