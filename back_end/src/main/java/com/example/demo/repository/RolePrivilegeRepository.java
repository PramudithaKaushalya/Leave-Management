package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.RolePrivilege;

public interface RolePrivilegeRepository extends JpaRepository<RolePrivilege, Integer>{
    
}