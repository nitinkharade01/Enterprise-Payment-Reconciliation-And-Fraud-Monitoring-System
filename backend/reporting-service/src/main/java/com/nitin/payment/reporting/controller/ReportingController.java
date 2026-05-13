/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.nitin.payment.common.ApiResponse
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.nitin.payment.reporting.controller;

import com.nitin.payment.common.ApiResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/reports"})
public class ReportingController {
    @GetMapping(value={"/dashboard"})
    ApiResponse<?> dashboard() {
        return ApiResponse.ok((String)"Dashboard fetched", Map.of("totalTransactions", 1250, "successCount", 1040, "failedCount", 86, "fraudCount", 18, "reconciliationPendingCount", 124, "dailyVolume", this.daily(), "paymentModeDistribution", this.paymentModes(), "fraudRisk", this.fraudRisk(), "recentTransactions", List.of(Map.of("transactionId", "TXN-SAMPLE-001", "customerName", "Aarav Mehta", "amount", 1520, "status", "SUCCESS"), Map.of("transactionId", "TXN-SAMPLE-003", "customerName", "Rohan Iyer", "amount", 76000, "status", "SUSPICIOUS"))));
    }

    @GetMapping(value={"/transactions/daily"})
    ApiResponse<?> dailyTransactions() {
        return ApiResponse.ok((String)"Daily transactions fetched", this.daily());
    }

    @GetMapping(value={"/payment-mode-distribution"})
    ApiResponse<?> paymentModeDistribution() {
        return ApiResponse.ok((String)"Payment mode distribution fetched", this.paymentModes());
    }

    @GetMapping(value={"/fraud-risk-summary"})
    ApiResponse<?> fraudRiskSummary() {
        return ApiResponse.ok((String)"Fraud risk summary fetched", this.fraudRisk());
    }

    private List<Map<String, Object>> daily() {
        return List.of(Map.of("date", LocalDate.now().minusDays(4L).toString(), "volume", new BigDecimal("120000")), Map.of("date", LocalDate.now().minusDays(3L).toString(), "volume", new BigDecimal("155000")), Map.of("date", LocalDate.now().minusDays(2L).toString(), "volume", new BigDecimal("98000")), Map.of("date", LocalDate.now().minusDays(1L).toString(), "volume", new BigDecimal("210000")), Map.of("date", LocalDate.now().toString(), "volume", new BigDecimal("175000")));
    }

    private Map<String, Integer> paymentModes() {
        return Map.of("UPI", 52, "CARD", 23, "NEFT", 15, "IMPS", 10);
    }

    private Map<String, Integer> fraudRisk() {
        return Map.of("LOW_RISK", 980, "MEDIUM_RISK", 42, "HIGH_RISK", 18, "BLOCKED", 4);
    }
}
