package com.cfbp.paymentreconciliation.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record SettlementRecordRequest(
        @NotBlank String settlementReference,
        @NotBlank String paymentReference,
        @NotNull @DecimalMin(value = "0.00") BigDecimal amount,
        @NotBlank String settlementStatus,
        @NotNull LocalDate settlementDate
) {
}
