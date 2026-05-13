package com.nitin.payment.transaction.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.nitin.payment.common.ApiResponse;
import com.nitin.payment.transaction.dto.UpdateStatusRequest;
import com.nitin.payment.transaction.entity.TransactionStatus;
import com.nitin.payment.transaction.service.TransactionService;
import com.nitin.payment.transaction.testdata.TestTransactionFactory;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TransactionControllerTest {

    @Test
    void createReturnsCreatedApiResponse() {
        TransactionService service = Mockito.mock(TransactionService.class);
        TransactionController controller = new TransactionController(service);
        when(service.create(TestTransactionFactory.createRequest())).thenReturn(TestTransactionFactory.response());

        ApiResponse<?> response = controller.create(TestTransactionFactory.createRequest());

        assertThat(response.status()).isEqualTo(201);
        assertThat(response.message()).isEqualTo("Transaction created");
    }

    @Test
    void updateStatusReturnsOkApiResponse() {
        TransactionService service = Mockito.mock(TransactionService.class);
        TransactionController controller = new TransactionController(service);
        UpdateStatusRequest request = new UpdateStatusRequest(TransactionStatus.SUCCESS, "done");
        when(service.updateStatus("TXN-TEST-001", request)).thenReturn(TestTransactionFactory.response());

        ApiResponse<?> response = controller.updateStatus("TXN-TEST-001", request);

        assertThat(response.status()).isEqualTo(200);
    }
}
