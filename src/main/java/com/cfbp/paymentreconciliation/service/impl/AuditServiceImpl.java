package com.cfbp.paymentreconciliation.service.impl;

import com.cfbp.paymentreconciliation.dto.AuditLogResponse;
import com.cfbp.paymentreconciliation.entity.AuditLog;
import com.cfbp.paymentreconciliation.enums.AuditAction;
import com.cfbp.paymentreconciliation.mapper.AuditLogMapper;
import com.cfbp.paymentreconciliation.repository.AuditLogRepository;
import com.cfbp.paymentreconciliation.service.AuditService;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AuditServiceImpl implements AuditService {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    public AuditServiceImpl(AuditLogRepository auditLogRepository, AuditLogMapper auditLogMapper) {
        this.auditLogRepository = auditLogRepository;
        this.auditLogMapper = auditLogMapper;
    }

    @Override
    public void log(String actor, AuditAction action, String entityType, String entityId, String message) {
        AuditLog log = new AuditLog();
        log.setActor(actor == null ? "system" : actor);
        log.setAction(action);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setMessage(message);
        auditLogRepository.save(log);
    }

    @Override
    public List<AuditLogResponse> getLogs() {
        return auditLogRepository.findAll().stream()
                .sorted(Comparator.comparing(AuditLog::getCreatedAt).reversed())
                .map(auditLogMapper::toResponse)
                .toList();
    }
}
