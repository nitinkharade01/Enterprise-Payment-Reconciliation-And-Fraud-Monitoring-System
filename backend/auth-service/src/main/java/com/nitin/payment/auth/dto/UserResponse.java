/*
 * Decompiled with CFR 0.152.
 */
package com.nitin.payment.auth.dto;

import java.util.Set;

public record UserResponse(Long id, String fullName, String email, boolean active, Set<String> roles) {
}
