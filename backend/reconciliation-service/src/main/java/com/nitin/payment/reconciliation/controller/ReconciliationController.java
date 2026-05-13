/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.nitin.payment.common.ApiResponse
 *  lombok.Generated
 *  org.springframework.http.MediaType
 *  org.springframework.http.ResponseEntity
 *  org.springframework.http.ResponseEntity$BodyBuilder
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 *  org.springframework.web.multipart.MultipartFile
 */
package com.nitin.payment.reconciliation.controller;

import com.nitin.payment.common.ApiResponse;
import com.nitin.payment.reconciliation.service.ReconciliationService;
import lombok.Generated;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value={"/api/reconciliation"})
public class ReconciliationController {
    private final ReconciliationService service;

    @PostMapping(value={"/upload"})
    ApiResponse<?> upload(@RequestParam MultipartFile file) {
        return ApiResponse.created((String)"Reconciliation completed", (Object)this.service.upload(file));
    }

    @GetMapping(value={"/summary/{fileId}"})
    ApiResponse<?> summary(@PathVariable Long fileId) {
        return ApiResponse.ok((String)"Summary fetched", (Object)this.service.summary(fileId));
    }

    @GetMapping(value={"/mismatches/{fileId}"})
    ApiResponse<?> mismatches(@PathVariable Long fileId) {
        return ApiResponse.ok((String)"Mismatches fetched", this.service.mismatches(fileId));
    }

    @GetMapping(value={"/download/{fileId}"})
    ResponseEntity<String> download(@PathVariable Long fileId) {
        return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=reconciliation-" + fileId + ".csv").contentType(MediaType.TEXT_PLAIN).body(this.service.report(fileId));
    }

    @Generated
    public ReconciliationController(ReconciliationService service) {
        this.service = service;
    }
}
