package com.nitin.payment.transaction.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.nitin.payment.transaction.dto.TransactionResponse;
import com.nitin.payment.transaction.testdata.TestTransactionFactory;
import org.junit.jupiter.api.Test;

class TransactionMapperTest {

    @Test
    void toResponseMapsCoreFields() {
        TransactionResponse response = new TransactionMapper().toResponse(TestTransactionFactory.transaction());

        assertThat(response.transactionId()).isEqualTo("TXN-TEST-001");
        assertThat(response.customerName()).isEqualTo("Aarav Mehta");
    }
}
