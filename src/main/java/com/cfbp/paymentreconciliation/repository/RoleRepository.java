package com.cfbp.paymentreconciliation.repository;

import com.cfbp.paymentreconciliation.entity.Role;
import com.cfbp.paymentreconciliation.enums.RoleName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
