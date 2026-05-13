/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.nitin.payment.common.ApiResponse
 *  lombok.Generated
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.nitin.payment.fraud.controller;

import com.nitin.payment.common.ApiResponse;
import com.nitin.payment.fraud.service.FraudAlertService;
import lombok.Generated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/fraud-alerts"})
public class FraudAlertController {
    private final FraudAlertService service;

    @GetMapping
    ApiResponse<?> all() {
        return ApiResponse.ok((String)"Fraud alerts fetched", this.service.findAll());
    }

    @GetMapping(value={"/{id}"})
    ApiResponse<?> find(@PathVariable String id) {
        return ApiResponse.ok((String)"Fraud alert fetched", (Object)this.service.find(id));
    }

    @PutMapping(value={"/{id}/status"})
    ApiResponse<?> update(@PathVariable String id, @RequestParam String status) {
        return ApiResponse.ok((String)"Fraud alert updated", (Object)this.service.updateStatus(id, status));
    }

    @Generated
    public FraudAlertController(FraudAlertService service) {
        this.service = service;
    }
}
