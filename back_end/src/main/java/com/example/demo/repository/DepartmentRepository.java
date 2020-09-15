package com.example.demo.repository;

import com.example.demo.model.Department;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long>{

	Department findByName(String department);

	List<?> findAllByOrderByName();
}