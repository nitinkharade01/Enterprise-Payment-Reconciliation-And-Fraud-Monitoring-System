package com.cfbp.paymentreconciliation.dto;

import java.util.List;

public record AuthResponse(
        String token,
        String tokenType,
        Long userId,
        String name,
        String email,
        List<String> roles
) {
}
