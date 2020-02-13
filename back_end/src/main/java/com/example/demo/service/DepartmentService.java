package com.example.demo.service;

import com.example.demo.payload.ApiResponse;
import com.example.demo.repository.DepartmentRepository;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentService.class);
    
    public ResponseEntity<?> getAll(long userId) {
        try {
            LOGGER.info(">>> Successfully get all departments. (By user ==> "+userId+")");
            return ResponseEntity.ok(new ApiResponse(true, departmentRepository.findAll()));
        } catch(Exception e) {
            LOGGER.error(">>> Unable to get all departments. (By user ==> "+userId+")", e.getMessage());
            return ResponseEntity.ok(new ApiResponse(false, "Unable to delete contact"));
        }
    }
}