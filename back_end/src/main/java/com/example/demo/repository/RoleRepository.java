package com.example.demo.repository;

import com.example.demo.model.Role;
import com.example.demo.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	Role findByName(String role_name);
    // Optional<Role> findByName(RoleName roleName);

	// Optional<Role> findByName(String role);
}
