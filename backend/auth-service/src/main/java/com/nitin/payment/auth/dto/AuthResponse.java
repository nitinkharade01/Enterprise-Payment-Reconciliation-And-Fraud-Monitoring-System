/*
 * Decompiled with CFR 0.152.
 */
package com.nitin.payment.auth.dto;

import java.util.Set;

public record AuthResponse(String token, Long userId, String fullName, String email, Set<String> roles) {
}
