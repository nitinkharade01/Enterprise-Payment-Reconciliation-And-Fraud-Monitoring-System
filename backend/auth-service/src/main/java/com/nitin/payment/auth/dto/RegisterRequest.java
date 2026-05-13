/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  jakarta.validation.constraints.Email
 *  jakarta.validation.constraints.NotBlank
 *  jakarta.validation.constraints.NotEmpty
 *  jakarta.validation.constraints.Size
 */
package com.nitin.payment.auth.dto;

import com.nitin.payment.auth.entity.RoleName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.Set;

public record RegisterRequest(@NotBlank String fullName, @Email @NotBlank String email, @NotBlank @Size(min=8) @NotBlank @Size(min=8) String password, @NotEmpty Set<RoleName> roles) {
}
