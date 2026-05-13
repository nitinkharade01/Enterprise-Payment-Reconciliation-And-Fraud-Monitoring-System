package com.cfbp.paymentreconciliation.dto;

import com.cfbp.paymentreconciliation.enums.ReconciliationStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReconciliationResponse(
        Long id,
        String paymentReference,
        String settlementReference,
        ReconciliationStatus status,
        BigDecimal differenceAmount,
        String remarks,
        LocalDateTime reconciledAt
) {
}
