package com.cfbp.paymentreconciliation.controller;

import com.cfbp.paymentreconciliation.dto.ReconciliationResponse;
import com.cfbp.paymentreconciliation.dto.ReconciliationRunRequest;
import com.cfbp.paymentreconciliation.service.ReconciliationService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reconciliation")
public class ReconciliationController {

    private final ReconciliationService reconciliationService;

    public ReconciliationController(ReconciliationService reconciliationService) {
        this.reconciliationService = reconciliationService;
    }

    @PostMapping("/run")
    public ResponseEntity<List<ReconciliationResponse>> run(@Valid @RequestBody ReconciliationRunRequest request) {
        return ResponseEntity.ok(reconciliationService.runReconciliation(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReconciliationResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(reconciliationService.getReconciliation(id));
    }

    @GetMapping
    public ResponseEntity<List<ReconciliationResponse>> getAll() {
        return ResponseEntity.ok(reconciliationService.getReconciliations());
    }
}
