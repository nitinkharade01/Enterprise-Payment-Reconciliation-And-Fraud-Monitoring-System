package com.cfbp.paymentreconciliation.dto;

import java.util.List;

public record UserResponse(
        Long id,
        String name,
        String email,
        List<String> roles
) {
}
