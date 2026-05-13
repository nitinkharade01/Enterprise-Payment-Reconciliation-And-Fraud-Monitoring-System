/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.nitin.payment.common.exception.DuplicateResourceException
 *  com.nitin.payment.common.exception.ResourceNotFoundException
 *  lombok.Generated
 *  org.springframework.security.crypto.password.PasswordEncoder
 *  org.springframework.stereotype.Service
 */
package com.nitin.payment.auth.service;

import com.nitin.payment.auth.dto.AuthResponse;
import com.nitin.payment.auth.dto.LoginRequest;
import com.nitin.payment.auth.dto.RegisterRequest;
import com.nitin.payment.auth.dto.UserResponse;
import com.nitin.payment.auth.entity.Role;
import com.nitin.payment.auth.entity.RoleName;
import com.nitin.payment.auth.entity.User;
import com.nitin.payment.auth.repository.RoleRepository;
import com.nitin.payment.auth.repository.UserRepository;
import com.nitin.payment.auth.security.JwtService;
import com.nitin.payment.common.exception.DuplicateResourceException;
import com.nitin.payment.common.exception.ResourceNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Generated;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserResponse register(RegisterRequest request) {
        if (this.userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email already registered");
        }
        User user = new User();
        user.setFullName(request.fullName());
        user.setEmail(request.email());
        user.setPassword(this.passwordEncoder.encode((CharSequence)request.password()));
        user.setRoles(this.loadRoles(request.roles()));
        return this.toResponse(this.userRepository.save(user));
    }

    public AuthResponse login(LoginRequest request) {
        User user = this.userRepository.findByEmail(request.email()).orElseThrow(() -> new ResourceNotFoundException("Invalid email or password"));
        if (!this.passwordEncoder.matches((CharSequence)request.password(), user.getPassword())) {
            throw new ResourceNotFoundException("Invalid email or password");
        }
        return new AuthResponse(this.jwtService.generate(user), user.getId(), user.getFullName(), user.getEmail(), user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet()));
    }

    public UserResponse profile(String email) {
        return this.userRepository.findByEmail(email).map(this::toResponse).orElseThrow(() -> new ResourceNotFoundException("User profile not found"));
    }

    public List<UserResponse> users() {
        return this.userRepository.findAll().stream().map(this::toResponse).toList();
    }

    private Set<Role> loadRoles(Set<RoleName> roleNames) {
        return roleNames.stream().map(name -> this.roleRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Role not found: " + name))).collect(Collectors.toSet());
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getFullName(), user.getEmail(), user.isActive(), user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet()));
    }

    @Generated
    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }
}
