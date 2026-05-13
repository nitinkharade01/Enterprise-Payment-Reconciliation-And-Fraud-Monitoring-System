package com.cfbp.paymentreconciliation.repository;

import com.cfbp.paymentreconciliation.entity.ReconciliationRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReconciliationRecordRepository extends JpaRepository<ReconciliationRecord, Long> {
}
