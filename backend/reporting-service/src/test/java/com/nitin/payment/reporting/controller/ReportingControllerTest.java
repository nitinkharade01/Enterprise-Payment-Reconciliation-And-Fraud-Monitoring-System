package com.nitin.payment.reporting.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.nitin.payment.common.ApiResponse;
import org.junit.jupiter.api.Test;

class ReportingControllerTest {

    private final ReportingController controller = new ReportingController();

    @Test
    void dashboardReturnsMetrics() {
        ApiResponse<?> response = controller.dashboard();

        assertThat(response.status()).isEqualTo(200);
        assertThat(response.message()).isEqualTo("Dashboard fetched");
    }

    @Test
    void paymentModeDistributionReturnsData() {
        ApiResponse<?> response = controller.paymentModeDistribution();

        assertThat(response.status()).isEqualTo(200);
    }
}
