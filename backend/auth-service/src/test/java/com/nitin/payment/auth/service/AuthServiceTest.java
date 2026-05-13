package com.nitin.payment.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nitin.payment.auth.dto.UserResponse;
import com.nitin.payment.auth.entity.RoleName;
import com.nitin.payment.auth.repository.RoleRepository;
import com.nitin.payment.auth.repository.UserRepository;
import com.nitin.payment.auth.security.JwtService;
import com.nitin.payment.auth.testdata.TestUserFactory;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;

    @Test
    void registerSavesEncodedUser() {
        AuthService service = new AuthService(userRepository, roleRepository, passwordEncoder, jwtService);
        when(userRepository.existsByEmail("nitin@test.com")).thenReturn(false);
        when(roleRepository.findByName(RoleName.ADMIN)).thenReturn(Optional.of(TestUserFactory.role(RoleName.ADMIN)));
        when(passwordEncoder.encode("Password@123")).thenReturn("encoded");
        when(userRepository.save(any())).thenReturn(TestUserFactory.user());

        UserResponse response = service.register(TestUserFactory.registerRequest());

        assertThat(response.email()).isEqualTo("nitin@test.com");
        assertThat(response.roles()).contains("ADMIN");
        verify(userRepository).save(any());
    }
}
