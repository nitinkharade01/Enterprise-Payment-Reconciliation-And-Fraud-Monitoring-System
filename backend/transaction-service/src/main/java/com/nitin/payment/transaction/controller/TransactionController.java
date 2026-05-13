/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.nitin.payment.common.ApiResponse
 *  jakarta.validation.Valid
 *  lombok.Generated
 *  org.springframework.data.domain.Pageable
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.nitin.payment.transaction.controller;

import com.nitin.payment.common.ApiResponse;
import com.nitin.payment.transaction.dto.CreateTransactionRequest;
import com.nitin.payment.transaction.dto.UpdateStatusRequest;
import com.nitin.payment.transaction.entity.TransactionStatus;
import com.nitin.payment.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.Generated;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/transactions"})
public class TransactionController {
    private final TransactionService service;

    @PostMapping
    ApiResponse<?> create(@Valid @RequestBody CreateTransactionRequest request) {
        return ApiResponse.created((String)"Transaction created", (Object)this.service.create(request));
    }

    @GetMapping
    ApiResponse<?> all(Pageable pageable) {
        return ApiResponse.ok((String)"Transactions fetched", this.service.findAll(pageable));
    }

    @GetMapping(value={"/{transactionId}"})
    ApiResponse<?> details(@PathVariable String transactionId) {
        return ApiResponse.ok((String)"Transaction fetched", (Object)this.service.findByTransactionId(transactionId));
    }

    @GetMapping(value={"/status/{status}"})
    ApiResponse<?> byStatus(@PathVariable TransactionStatus status, Pageable pageable) {
        return ApiResponse.ok((String)"Transactions fetched", this.service.byStatus(status, pageable));
    }

    @PutMapping(value={"/{transactionId}/status"})
    ApiResponse<?> updateStatus(@PathVariable String transactionId, @Valid @RequestBody UpdateStatusRequest request) {
        return ApiResponse.ok((String)"Transaction status updated", (Object)this.service.updateStatus(transactionId, request));
    }

    @GetMapping(value={"/search"})
    ApiResponse<?> search(@RequestParam String keyword, Pageable pageable) {
        return ApiResponse.ok((String)"Transactions fetched", this.service.search(keyword, pageable));
    }

    @Generated
    public TransactionController(TransactionService service) {
        this.service = service;
    }
}
