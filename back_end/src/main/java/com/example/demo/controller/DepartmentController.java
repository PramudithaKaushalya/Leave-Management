package com.example.demo.controller;

import com.example.demo.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/")
public class DepartmentController {

    @Autowired
    private DepartmentRepository departmentRepository;
}