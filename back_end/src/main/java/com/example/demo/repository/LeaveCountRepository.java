package com.example.demo.repository;

import com.example.demo.model.Employee;
import com.example.demo.model.LeaveCount;
import com.example.demo.model.LeaveType;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveCountRepository extends JpaRepository<LeaveCount, Integer>{

	boolean existsByEmployee(Employee emp);

	boolean existsByType(LeaveType type);

	LeaveCount findByEmployeeAndType(Employee emp, LeaveType type);

	boolean existsByEmployeeAndType(Employee emp, LeaveType casual);
    
}