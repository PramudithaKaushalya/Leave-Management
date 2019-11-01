package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Department;
import com.example.demo.repository.DepartmentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public List<Department> getAll() {
        System.out.println("Get All Departments !!!");
        return departmentRepository.findAll();
    }
}