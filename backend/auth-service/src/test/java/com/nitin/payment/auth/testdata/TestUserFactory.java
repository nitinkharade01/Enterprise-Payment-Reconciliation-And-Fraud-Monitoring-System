package com.nitin.payment.auth.testdata;

import com.nitin.payment.auth.dto.RegisterRequest;
import com.nitin.payment.auth.entity.Role;
import com.nitin.payment.auth.entity.RoleName;
import com.nitin.payment.auth.entity.User;
import java.util.Set;

public final class TestUserFactory {

    private TestUserFactory() {
    }

    public static RegisterRequest registerRequest() {
        return new RegisterRequest("Nitin Kharade", "nitin@test.com", "Password@123", Set.of(RoleName.ADMIN));
    }

    public static Role role(RoleName roleName) {
        Role role = new Role();
        role.setName(roleName);
        return role;
    }

    public static User user() {
        User user = new User();
        user.setId(1L);
        user.setFullName("Nitin Kharade");
        user.setEmail("nitin@test.com");
        user.setPassword("encoded");
        user.setRoles(Set.of(role(RoleName.ADMIN)));
        return user;
    }
}
