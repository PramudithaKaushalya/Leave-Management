package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.example.demo.model.Employee;
import com.example.demo.model.Role;

public interface EmployeeRepository extends JpaRepository<Employee, Integer>{

	List<Employee> findByRole(Role role);

	Employee findByEmail(String email);

	boolean existsByEmail(String email);

	Employee findBySupervisor1(String supervisor1);

	Employee findBySupervisor2(String supervisor2);
    
}
		 