package com.nitin.payment.fraud.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.nitin.payment.common.ApiResponse;
import com.nitin.payment.fraud.service.FraudAlertService;
import com.nitin.payment.fraud.testdata.TestFraudAlertFactory;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class FraudAlertControllerTest {

    @Test
    void allReturnsApiResponse() {
        FraudAlertService service = Mockito.mock(FraudAlertService.class);
        FraudAlertController controller = new FraudAlertController(service);
        when(service.findAll()).thenReturn(List.of(TestFraudAlertFactory.alert()));

        ApiResponse<?> response = controller.all();

        assertThat(response.status()).isEqualTo(200);
    }
}
