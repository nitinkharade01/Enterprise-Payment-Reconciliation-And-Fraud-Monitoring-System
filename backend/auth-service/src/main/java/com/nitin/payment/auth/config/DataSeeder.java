/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.springframework.boot.CommandLineRunner
 *  org.springframework.context.annotation.Bean
 *  org.springframework.context.annotation.Configuration
 *  org.springframework.security.crypto.password.PasswordEncoder
 */
package com.nitin.payment.auth.config;

import com.nitin.payment.auth.entity.Role;
import com.nitin.payment.auth.entity.RoleName;
import com.nitin.payment.auth.entity.User;
import com.nitin.payment.auth.repository.RoleRepository;
import com.nitin.payment.auth.repository.UserRepository;
import java.util.Set;
import lombok.Generated;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {
    @Bean
    CommandLineRunner seed(RoleRepository roles, UserRepository users, PasswordEncoder encoder) {
        return args -> {
            for (RoleName roleName : RoleName.values()) {
                roles.findByName(roleName).orElseGet(() -> {
                    Role role = new Role();
                    role.setName(roleName);
                    return (Role)roles.save(role);
                });
            }
            this.createUser(users, roles, encoder, "Admin User", "admin@payment.com", "Admin@123", RoleName.ADMIN);
            this.createUser(users, roles, encoder, "Finance User", "finance@payment.com", "Finance@123", RoleName.FINANCE_USER);
            this.createUser(users, roles, encoder, "Audit User", "auditor@payment.com", "Auditor@123", RoleName.AUDITOR);
        };
    }

    private void createUser(UserRepository users, RoleRepository roles, PasswordEncoder encoder, String name, String email, String password, RoleName roleName) {
        if (users.existsByEmail(email)) {
            return;
        }
        User user = new User();
        user.setFullName(name);
        user.setEmail(email);
        user.setPassword(encoder.encode((CharSequence)password));
        user.setRoles(Set.of(roles.findByName(roleName).orElseThrow()));
        users.save(user);
    }

    @Generated
    public DataSeeder() {
    }
}
