package com.cfbp.paymentreconciliation.service.impl;

import com.cfbp.paymentreconciliation.dto.AuthResponse;
import com.cfbp.paymentreconciliation.dto.LoginRequest;
import com.cfbp.paymentreconciliation.dto.RegisterRequest;
import com.cfbp.paymentreconciliation.dto.UserResponse;
import com.cfbp.paymentreconciliation.entity.Role;
import com.cfbp.paymentreconciliation.entity.User;
import com.cfbp.paymentreconciliation.enums.AuditAction;
import com.cfbp.paymentreconciliation.enums.RoleName;
import com.cfbp.paymentreconciliation.exception.DuplicateResourceException;
import com.cfbp.paymentreconciliation.repository.RoleRepository;
import com.cfbp.paymentreconciliation.repository.UserRepository;
import com.cfbp.paymentreconciliation.security.JwtService;
import com.cfbp.paymentreconciliation.service.AuditService;
import com.cfbp.paymentreconciliation.service.AuthService;
import java.util.List;
import java.util.Set;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuditService auditService;

    public AuthServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtService jwtService,
                           AuditService auditService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.auditService = auditService;
    }

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email already registered");
        }
        RoleName roleName = request.role() == null ? RoleName.FINANCE_USER : request.role();
        Role role = roleRepository.findByName(roleName).orElseGet(() -> roleRepository.save(new Role(roleName)));

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRoles(Set.of(role));

        User saved = userRepository.save(user);
        auditService.log(saved.getEmail(), AuditAction.USER_REGISTERED, "User", String.valueOf(saved.getId()), "User registered");
        return toResponse(saved);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        User user = userRepository.findByEmail(request.email()).orElseThrow();
        UserDetails details = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRoles().stream().map(role -> "ROLE_" + role.getName().name()).toArray(String[]::new))
                .build();
        String token = jwtService.generateToken(details);
        auditService.log(user.getEmail(), AuditAction.USER_LOGIN, "User", String.valueOf(user.getId()), "User logged in");
        return new AuthResponse(token, "Bearer", user.getId(), user.getName(), user.getEmail(), roleNames(user));
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), roleNames(user));
    }

    private List<String> roleNames(User user) {
        return user.getRoles().stream().map(role -> role.getName().name()).toList();
    }
}
