/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.data.jpa.repository.JpaRepository
 */
package com.nitin.payment.auth.repository;

import com.nitin.payment.auth.entity.Role;
import com.nitin.payment.auth.entity.RoleName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository
extends JpaRepository<Role, Long> {
    public Optional<Role> findByName(RoleName var1);
}
