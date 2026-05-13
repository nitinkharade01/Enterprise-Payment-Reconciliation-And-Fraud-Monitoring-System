/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.nitin.payment.common.ApiResponse
 *  jakarta.validation.Valid
 *  lombok.Generated
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestHeader
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.nitin.payment.auth.controller;

import com.nitin.payment.auth.dto.LoginRequest;
import com.nitin.payment.auth.dto.RegisterRequest;
import com.nitin.payment.auth.service.AuthService;
import com.nitin.payment.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.Generated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/auth"})
public class AuthController {
    private final AuthService authService;

    @PostMapping(value={"/register"})
    ApiResponse<?> register(@Valid @RequestBody RegisterRequest request) {
        return ApiResponse.created((String)"User registered", (Object)this.authService.register(request));
    }

    @PostMapping(value={"/login"})
    ApiResponse<?> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok((String)"Login successful", (Object)this.authService.login(request));
    }

    @GetMapping(value={"/profile"})
    ApiResponse<?> profile(@RequestHeader(value="X-Authenticated-User") String email) {
        return ApiResponse.ok((String)"Profile fetched", (Object)this.authService.profile(email));
    }

    @GetMapping(value={"/users"})
    ApiResponse<?> users() {
        return ApiResponse.ok((String)"Users fetched", this.authService.users());
    }

    @Generated
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
}
