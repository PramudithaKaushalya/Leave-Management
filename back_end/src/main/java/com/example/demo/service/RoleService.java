package com.example.demo.service;

import com.example.demo.payload.ApiResponse;
import com.example.demo.repository.RoleRepository;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

	private static final Logger LOGGER = LoggerFactory.getLogger(RoleService.class);

    public ResponseEntity<?> getAll( Long userId) {

        try {
            LOGGER.info(">>> Successfully get roles. (By user ==> "+userId+")");
            return ResponseEntity.ok(new ApiResponse(true, roleRepository.findAll()));
        } catch(Exception e) {
            LOGGER.error(">>> Unable to get roles. (By user ==> "+userId+")", e.getMessage());
            return ResponseEntity.ok(new ApiResponse(false, "Unable to get roles"));
        } 
    }
}