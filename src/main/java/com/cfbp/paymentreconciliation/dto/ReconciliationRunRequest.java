package com.cfbp.paymentreconciliation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record ReconciliationRunRequest(
        @NotEmpty List<@Valid SettlementRecordRequest> settlementRecords
) {
}
