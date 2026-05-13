package com.nitin.payment.reconciliation.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.nitin.payment.common.ApiResponse;
import com.nitin.payment.reconciliation.service.ReconciliationService;
import com.nitin.payment.reconciliation.testdata.TestReconciliationFileFactory;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ReconciliationControllerTest {

    @Test
    void summaryReturnsApiResponse() {
        ReconciliationService service = Mockito.mock(ReconciliationService.class);
        ReconciliationController controller = new ReconciliationController(service);
        when(service.summary(10L)).thenReturn(TestReconciliationFileFactory.summary());

        ApiResponse<?> response = controller.summary(10L);

        assertThat(response.status()).isEqualTo(200);
    }

    @Test
    void mismatchesReturnsApiResponse() {
        ReconciliationService service = Mockito.mock(ReconciliationService.class);
        ReconciliationController controller = new ReconciliationController(service);
        when(service.mismatches(10L)).thenReturn(List.of(TestReconciliationFileFactory.mismatch()));

        ApiResponse<?> response = controller.mismatches(10L);

        assertThat(response.status()).isEqualTo(200);
    }
}
