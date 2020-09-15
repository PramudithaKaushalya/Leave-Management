package com.example.demo.controller;

import com.example.demo.payload.ApiResponse;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.LeaveTypeService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/leave_type")
public class LeaveTypeController {

    @Autowired
    private LeaveTypeService leaveTypeService;

    @Autowired
    JwtTokenProvider tokenProvider;

    private static final Logger LOGGER = LoggerFactory.getLogger(LeaveTypeController.class);
    
    @GetMapping("/all")
    public ResponseEntity<?> getAll(@RequestHeader("Authorization") String token) {
        if(StringUtils.hasText(token) && token.startsWith("Bearer ")){
            String jwt = token.substring(7);
            Long id = tokenProvider.getUserIdFromJWT(jwt);

            return leaveTypeService.getAll(id);
        } else {
            LOGGER.warn(">>> User authentication failed");
            return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
        }
    }
}