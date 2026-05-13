package com.cfbp.paymentreconciliation.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cfbp.paymentreconciliation.dto.RegisterRequest;
import com.cfbp.paymentreconciliation.dto.UserResponse;
import com.cfbp.paymentreconciliation.entity.Role;
import com.cfbp.paymentreconciliation.entity.User;
import com.cfbp.paymentreconciliation.enums.RoleName;
import com.cfbp.paymentreconciliation.repository.RoleRepository;
import com.cfbp.paymentreconciliation.repository.UserRepository;
import com.cfbp.paymentreconciliation.security.JwtService;
import com.cfbp.paymentreconciliation.service.AuditService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuditService auditService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void registerCreatesUserWithRequestedRole() {
        RegisterRequest request = new RegisterRequest("Nitin", "nitin@test.com", "secret123", RoleName.ADMIN);
        Role adminRole = new Role(RoleName.ADMIN);
        when(userRepository.existsByEmail("nitin@test.com")).thenReturn(false);
        when(roleRepository.findByName(RoleName.ADMIN)).thenReturn(Optional.of(adminRole));
        when(passwordEncoder.encode("secret123")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = authService.register(request);

        assertThat(response.email()).isEqualTo("nitin@test.com");
        assertThat(response.roles()).containsExactly("ADMIN");
        verify(auditService).log(any(), any(), any(), any(), any());
    }
}
