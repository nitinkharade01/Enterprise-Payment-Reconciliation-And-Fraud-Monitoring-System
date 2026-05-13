/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  jakarta.validation.constraints.DecimalMin
 *  jakarta.validation.constraints.NotBlank
 *  jakarta.validation.constraints.NotNull
 */
package com.nitin.payment.transaction.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CreateTransactionRequest(@NotNull Long userId, @NotBlank String customerName, @NotBlank String sourceAccount, @NotBlank String destinationAccount, @NotNull @DecimalMin(value="1.00") @NotNull @DecimalMin(value="1.00") BigDecimal amount, @NotBlank String currency, @NotBlank String paymentMode, String remarks) {
}
