package com.nitin.payment.auth.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.nitin.payment.auth.dto.AuthResponse;
import com.nitin.payment.auth.dto.LoginRequest;
import com.nitin.payment.auth.dto.RegisterRequest;
import com.nitin.payment.auth.dto.UserResponse;
import com.nitin.payment.auth.entity.RoleName;
import com.nitin.payment.auth.service.AuthService;
import com.nitin.payment.common.ApiResponse;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AuthControllerTest {

    @Test
    void loginReturnsApiResponse() {
        AuthService service = Mockito.mock(AuthService.class);
        AuthController controller = new AuthController(service);
        LoginRequest request = new LoginRequest("admin@payment.com", "Admin@123");
        when(service.login(request)).thenReturn(new AuthResponse("token", 1L, "Admin", "admin@payment.com", Set.of("ADMIN")));

        ApiResponse<?> response = controller.login(request);

        assertThat(response.status()).isEqualTo(200);
        assertThat(response.message()).isEqualTo("Login successful");
    }

    @Test
    void registerReturnsCreatedResponse() {
        AuthService service = Mockito.mock(AuthService.class);
        AuthController controller = new AuthController(service);
        RegisterRequest request = new RegisterRequest("Admin", "admin@payment.com", "Admin@123", Set.of(RoleName.ADMIN));
        when(service.register(request)).thenReturn(new UserResponse(1L, "Admin", "admin@payment.com", true, Set.of("ADMIN")));

        ApiResponse<?> response = controller.register(request);

        assertThat(response.status()).isEqualTo(201);
    }
}
