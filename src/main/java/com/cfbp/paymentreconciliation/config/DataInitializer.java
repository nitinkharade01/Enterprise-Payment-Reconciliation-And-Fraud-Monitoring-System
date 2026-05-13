package com.cfbp.paymentreconciliation.config;

import com.cfbp.paymentreconciliation.entity.Role;
import com.cfbp.paymentreconciliation.entity.User;
import com.cfbp.paymentreconciliation.enums.RoleName;
import com.cfbp.paymentreconciliation.repository.RoleRepository;
import com.cfbp.paymentreconciliation.repository.UserRepository;
import java.util.Set;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedData(RoleRepository roleRepository,
                               UserRepository userRepository,
                               PasswordEncoder passwordEncoder) {
        return args -> {
            for (RoleName roleName : RoleName.values()) {
                roleRepository.findByName(roleName).orElseGet(() -> roleRepository.save(new Role(roleName)));
            }

            if (!userRepository.existsByEmail("admin@payment.com")) {
                Role adminRole = roleRepository.findByName(RoleName.ADMIN).orElseThrow();
                User admin = new User();
                admin.setName("System Admin");
                admin.setEmail("admin@payment.com");
                admin.setPassword(passwordEncoder.encode("Admin@123"));
                admin.setRoles(Set.of(adminRole));
                userRepository.save(admin);
            }
        };
    }
}
