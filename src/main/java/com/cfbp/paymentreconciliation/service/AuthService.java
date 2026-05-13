package com.cfbp.paymentreconciliation.service;

import com.cfbp.paymentreconciliation.dto.AuthResponse;
import com.cfbp.paymentreconciliation.dto.LoginRequest;
import com.cfbp.paymentreconciliation.dto.RegisterRequest;
import com.cfbp.paymentreconciliation.dto.UserResponse;

public interface AuthService {
    UserResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
