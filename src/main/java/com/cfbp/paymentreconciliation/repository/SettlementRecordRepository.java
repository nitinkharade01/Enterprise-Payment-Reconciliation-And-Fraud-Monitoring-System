package com.cfbp.paymentreconciliation.repository;

import com.cfbp.paymentreconciliation.entity.SettlementRecord;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementRecordRepository extends JpaRepository<SettlementRecord, Long> {
    Optional<SettlementRecord> findBySettlementReference(String settlementReference);
}
