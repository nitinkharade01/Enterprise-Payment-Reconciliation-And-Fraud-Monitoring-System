/*
 * Decompiled with CFR 0.152.
 */
package com.nitin.payment.common;

import java.time.Instant;
import java.util.List;

public record ApiResponse<T>(Instant timestamp, int status, String message, T data, List<String> errors) {
    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<T>(Instant.now(), 200, message, data, List.of());
    }

    public static <T> ApiResponse<T> created(String message, T data) {
        return new ApiResponse<T>(Instant.now(), 201, message, data, List.of());
    }

    public static <T> ApiResponse<T> error(int status, String message, List<String> errors) {
        return new ApiResponse<T>(Instant.now(), status, message, null, errors == null ? List.of() : errors);
    }
}
