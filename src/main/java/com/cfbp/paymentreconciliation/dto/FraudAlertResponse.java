package com.cfbp.paymentreconciliation.dto;

import com.cfbp.paymentreconciliation.enums.FraudAlertStatus;
import com.cfbp.paymentreconciliation.enums.FraudSeverity;
import java.time.LocalDateTime;

public record FraudAlertResponse(
        Long id,
        Long paymentId,
        String paymentReference,
        FraudSeverity severity,
        FraudAlertStatus status,
        String ruleName,
        String description,
        LocalDateTime createdAt
) {
}
