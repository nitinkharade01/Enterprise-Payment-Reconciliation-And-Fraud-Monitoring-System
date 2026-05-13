package com.cfbp.paymentreconciliation.service;

import com.cfbp.paymentreconciliation.dto.AuditLogResponse;
import com.cfbp.paymentreconciliation.enums.AuditAction;
import java.util.List;

public interface AuditService {
    void log(String actor, AuditAction action, String entityType, String entityId, String message);

    List<AuditLogResponse> getLogs();
}
