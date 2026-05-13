package com.cfbp.paymentreconciliation.controller;

import com.cfbp.paymentreconciliation.dto.FraudAlertResponse;
import com.cfbp.paymentreconciliation.service.FraudService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fraud")
public class FraudController {

    private final FraudService fraudService;

    public FraudController(FraudService fraudService) {
        this.fraudService = fraudService;
    }

    @PostMapping("/check/{paymentId}")
    public ResponseEntity<FraudAlertResponse> checkPayment(@PathVariable Long paymentId) {
        return ResponseEntity.ok(fraudService.checkPayment(paymentId));
    }

    @GetMapping("/alerts")
    public ResponseEntity<List<FraudAlertResponse>> getAlerts() {
        return ResponseEntity.ok(fraudService.getAlerts());
    }

    @GetMapping("/alerts/{id}")
    public ResponseEntity<FraudAlertResponse> getAlert(@PathVariable Long id) {
        return ResponseEntity.ok(fraudService.getAlert(id));
    }
}
