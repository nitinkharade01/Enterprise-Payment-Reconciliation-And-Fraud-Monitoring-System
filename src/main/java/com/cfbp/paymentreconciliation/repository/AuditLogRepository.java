package com.cfbp.paymentreconciliation.repository;

import com.cfbp.paymentreconciliation.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
