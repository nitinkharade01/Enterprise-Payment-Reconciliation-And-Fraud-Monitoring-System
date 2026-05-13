package com.cfbp.paymentreconciliation.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record PaymentRequest(
        String paymentReference,
        @NotBlank String customerName,
        @NotBlank String sourceAccount,
        @NotBlank String destinationAccount,
        @NotNull @DecimalMin(value = "0.01") BigDecimal amount,
        @NotBlank String currency,
        @NotBlank String paymentMode
) {
}
