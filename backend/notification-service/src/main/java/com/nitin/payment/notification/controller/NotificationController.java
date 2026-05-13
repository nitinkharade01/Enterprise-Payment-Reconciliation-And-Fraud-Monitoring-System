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
 *  org.springframework.web.bind.annotation.RestController
 */
package com.nitin.payment.notification.controller;

import com.nitin.payment.common.ApiResponse;
import com.nitin.payment.notification.service.NotificationService;
import lombok.Generated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/notifications"})
public class NotificationController {
    private final NotificationService service;

    @GetMapping
    ApiResponse<?> all() {
        return ApiResponse.ok((String)"Notifications fetched", this.service.all());
    }

    @PutMapping(value={"/{id}/read"})
    ApiResponse<?> read(@PathVariable Long id) {
        return ApiResponse.ok((String)"Notification marked read", this.service.markRead(id));
    }

    @Generated
    public NotificationController(NotificationService service) {
        this.service = service;
    }
}
