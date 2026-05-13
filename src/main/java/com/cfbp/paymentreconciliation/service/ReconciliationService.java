package com.cfbp.paymentreconciliation.service;

import com.cfbp.paymentreconciliation.dto.ReconciliationResponse;
import com.cfbp.paymentreconciliation.dto.ReconciliationRunRequest;
import java.util.List;

public interface ReconciliationService {
    List<ReconciliationResponse> runReconciliation(ReconciliationRunRequest request);

    ReconciliationResponse getReconciliation(Long id);

    List<ReconciliationResponse> getReconciliations();
}
