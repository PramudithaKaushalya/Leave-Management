package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Privilege;

public interface PrivilegeRepository extends JpaRepository<Privilege, Integer>{
    
}