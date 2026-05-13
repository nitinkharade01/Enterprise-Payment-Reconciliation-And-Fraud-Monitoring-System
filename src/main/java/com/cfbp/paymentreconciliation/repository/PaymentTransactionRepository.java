package com.cfbp.paymentreconciliation.repository;

import com.cfbp.paymentreconciliation.entity.PaymentTransaction;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
    Optional<PaymentTransaction> findByPaymentReference(String paymentReference);

    boolean existsByPaymentReference(String paymentReference);
}
