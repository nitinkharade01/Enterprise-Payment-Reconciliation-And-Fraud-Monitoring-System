package com.cfbp.paymentreconciliation.mapper;

import com.cfbp.paymentreconciliation.dto.ReconciliationResponse;
import com.cfbp.paymentreconciliation.entity.ReconciliationRecord;
import org.springframework.stereotype.Component;

@Component
public class ReconciliationMapper {

    public ReconciliationResponse toResponse(ReconciliationRecord record) {
        String paymentReference = record.getPaymentTransaction() == null
                ? null
                : record.getPaymentTransaction().getPaymentReference();
        String settlementReference = record.getSettlementRecord() == null
                ? null
                : record.getSettlementRecord().getSettlementReference();
        return new ReconciliationResponse(
                record.getId(),
                paymentReference,
                settlementReference,
                record.getStatus(),
                record.getDifferenceAmount(),
                record.getRemarks(),
                record.getReconciledAt()
        );
    }
}
