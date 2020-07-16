package com.example.demo.controller;

import com.example.demo.payload.ApiResponse;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.RoleService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    JwtTokenProvider tokenProvider;

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);

    @GetMapping("/all")
    public ResponseEntity<?> getAll(@RequestHeader("Authorization") String token) {

        if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {

            String jwt = token.substring(7);
            Long userId = tokenProvider.getUserIdFromJWT(jwt);
            return roleService.getAll(userId);
        }
        else {
            LOGGER.warn(">>> User authentication failed");
            return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
        }
    }
}