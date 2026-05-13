package com.cfbp.paymentreconciliation.mapper;

import com.cfbp.paymentreconciliation.dto.FraudAlertResponse;
import com.cfbp.paymentreconciliation.entity.FraudAlert;
import org.springframework.stereotype.Component;

@Component
public class FraudAlertMapper {

    public FraudAlertResponse toResponse(FraudAlert alert) {
        return new FraudAlertResponse(
                alert.getId(),
                alert.getPaymentTransaction().getId(),
                alert.getPaymentTransaction().getPaymentReference(),
                alert.getSeverity(),
                alert.getStatus(),
                alert.getRuleName(),
                alert.getDescription(),
                alert.getCreatedAt()
        );
    }
}
