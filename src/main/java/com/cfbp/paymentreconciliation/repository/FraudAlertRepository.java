package com.cfbp.paymentreconciliation.repository;

import com.cfbp.paymentreconciliation.entity.FraudAlert;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FraudAlertRepository extends JpaRepository<FraudAlert, Long> {
    List<FraudAlert> findByPaymentTransactionId(Long paymentId);
}
