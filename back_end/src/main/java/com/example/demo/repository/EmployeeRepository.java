package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.example.demo.model.Employee;
import com.example.demo.model.Role;

public interface EmployeeRepository extends JpaRepository<Employee, Integer>{

	List<Employee> findByRole(Role role);
    
}
		 