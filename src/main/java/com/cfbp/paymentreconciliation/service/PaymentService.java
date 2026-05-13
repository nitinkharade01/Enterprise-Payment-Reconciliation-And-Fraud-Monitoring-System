package com.cfbp.paymentreconciliation.service;

import com.cfbp.paymentreconciliation.dto.PaymentRequest;
import com.cfbp.paymentreconciliation.dto.PaymentResponse;
import com.cfbp.paymentreconciliation.dto.PaymentValidationResponse;
import java.util.List;

public interface PaymentService {
    PaymentResponse createPayment(PaymentRequest request);

    PaymentResponse getPayment(Long id);

    List<PaymentResponse> getPayments();

    PaymentValidationResponse validatePayment(Long id);
}
