package com.example.demo.controller;

import com.example.demo.payload.*;
import com.example.demo.service.UserService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import com.example.demo.security.JwtTokenProvider;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider tokenProvider;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/all")
    public ResponseEntity<?> getAll(@RequestHeader("Authorization") String token) {

        if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {

            String jwt = token.substring(7);
            Long userId = tokenProvider.getUserIdFromJWT(jwt);
            return userService.getAll(userId);
        }
        else {
            LOGGER.warn(">>> User authentication failed");
            return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getOne(@RequestHeader("Authorization") String token, @PathVariable("id") Long id) {
        if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {

            String jwt = token.substring(7);
            Long userId = tokenProvider.getUserIdFromJWT(jwt);        
            return userService.searchById(id, userId);
        }
        else {
            LOGGER.warn(">>> User authentication failed");
            return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
        }
    }

    @GetMapping("/name")
    public ResponseEntity<?> getUserName(@RequestHeader("Authorization") String token) {
        if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {

            String jwt = token.substring(7);
            Long userId = tokenProvider.getUserIdFromJWT(jwt);        
            return userService.getUserName(userId);
        }
        else {
            LOGGER.warn(">>> User authentication failed");
            return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
        }
    }

    @PostMapping(value = "/resign")
    public ResponseEntity<?> resignUser(@RequestHeader("Authorization") String token, @RequestBody  Employee employee) {

        if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {

            String jwt = token.substring(7);
            Long userId = tokenProvider.getUserIdFromJWT(jwt);
            return userService.resignEmployee(employee, userId);
        }
        else {
            LOGGER.warn(">>> User authentication failed");
            return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
        }
    }

    @PostMapping(value = "/update/{id}")
    public ResponseEntity<?> updateUser(@RequestHeader("Authorization") String token, @RequestBody UpdateUser employee, @PathVariable("id") Long id) {

        if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {

            String jwt = token.substring(7);
            Long userId = tokenProvider.getUserIdFromJWT(jwt);
            return userService.updateEmployee(id, employee, userId);
        }
        else {
            LOGGER.warn(">>> User authentication failed");
            return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
        }
    }

    @GetMapping("/supervisor")
    public ResponseEntity<?> findSupervisors(@RequestHeader("Authorization") String token) {

        if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {

            String jwt = token.substring(7);
            Long userId = tokenProvider.getUserIdFromJWT(jwt);
            return userService.searchSupervisors(userId);
        }
        else {
            LOGGER.warn(">>> User authentication failed");
            return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
        }
    }
}
