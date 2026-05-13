package com.cfbp.paymentreconciliation.mapper;

import com.cfbp.paymentreconciliation.dto.AuditLogResponse;
import com.cfbp.paymentreconciliation.entity.AuditLog;
import org.springframework.stereotype.Component;

@Component
public class AuditLogMapper {

    public AuditLogResponse toResponse(AuditLog log) {
        return new AuditLogResponse(
                log.getId(),
                log.getActor(),
                log.getAction(),
                log.getEntityType(),
                log.getEntityId(),
                log.getMessage(),
                log.getCreatedAt()
        );
    }
}
