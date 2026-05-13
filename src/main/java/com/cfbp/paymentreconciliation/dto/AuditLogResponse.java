package com.cfbp.paymentreconciliation.dto;

import com.cfbp.paymentreconciliation.enums.AuditAction;
import java.time.LocalDateTime;

public record AuditLogResponse(
        Long id,
        String actor,
        AuditAction action,
        String entityType,
        String entityId,
        String message,
        LocalDateTime createdAt
) {
}
