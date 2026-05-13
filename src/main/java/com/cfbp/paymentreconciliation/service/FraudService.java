package com.cfbp.paymentreconciliation.service;

import com.cfbp.paymentreconciliation.dto.FraudAlertResponse;
import java.util.List;

public interface FraudService {
    FraudAlertResponse checkPayment(Long paymentId);

    List<FraudAlertResponse> getAlerts();

    FraudAlertResponse getAlert(Long id);
}
