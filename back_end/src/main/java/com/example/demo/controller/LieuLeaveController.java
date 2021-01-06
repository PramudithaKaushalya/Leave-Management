package com.example.demo.controller;

import com.example.demo.model.LieuLeave;
import com.example.demo.payload.ApiResponse;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.LieuLeaveService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/lieu_leave")
public class LieuLeaveController {

    @Autowired
    private  LieuLeaveService lieuLeaveService;
    
    @Autowired
    JwtTokenProvider tokenProvider;

    private static final Logger LOGGER = LoggerFactory.getLogger(LieuLeaveController.class);

    @GetMapping("/get_all")
    public ResponseEntity<?> getAll(@RequestHeader("Authorization") String token) {
        if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {
			String jwt = token.substring(7);
			Long userId = tokenProvider.getUserIdFromJWT(jwt);

            return lieuLeaveService.getAllLieuLeaves(userId);
        }
        else {
            LOGGER.warn(">>> User authentication failed");
            return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
        }
    }

    @GetMapping("/get_own")
    public ResponseEntity<?> getOwn(@RequestHeader("Authorization") String token) {
        if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {
			String jwt = token.substring(7);
			Long userId = tokenProvider.getUserIdFromJWT(jwt);

            return lieuLeaveService.getOwnLieuLeaves(userId);
        }
        else {
            LOGGER.warn(">>> User authentication failed");
            return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> addLieuLeave(@RequestHeader("Authorization") String token, @RequestBody LieuLeave leave) {
        if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {

            String jwt = token.substring(7);
            Long userId = tokenProvider.getUserIdFromJWT(jwt);

            return lieuLeaveService.addLieuLeave(leave, userId);
        }
        else {
            LOGGER.warn(">>> User authentication failed");
            return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
        }
    }

    @PostMapping("/save_by_admin")
    public ResponseEntity<?> addLieuLeaveByAdmin(@RequestHeader("Authorization") String token, @RequestBody LieuLeave leave) {
        if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {

            String jwt = token.substring(7);
            Long userId = tokenProvider.getUserIdFromJWT(jwt);

            return lieuLeaveService.addLieuLeaveByAdmin(leave, userId);
        }
        else {
            LOGGER.warn(">>> User authentication failed");
            return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
        }
    }

    @GetMapping("/approve/{id}")
    public ResponseEntity<?> approveLieuLeave(@RequestHeader("Authorization") String token, @PathVariable("id") Long id) {
        if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {

            String jwt = token.substring(7);
            Long userId = tokenProvider.getUserIdFromJWT(jwt);

            return lieuLeaveService.approveLieuLeaves(id, userId);
        }
        else {
            LOGGER.warn(">>> User authentication failed");
            return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
        }
    }

    @GetMapping("/reject/{id}")
    public ResponseEntity<?> rejectLieuLeave(@RequestHeader("Authorization") String token, @PathVariable("id") Long id) {
        if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {

            String jwt = token.substring(7);
            Long userId = tokenProvider.getUserIdFromJWT(jwt);

            return lieuLeaveService.rejectLieuLeaves(id, userId);
        }
        else {
            LOGGER.warn(">>> User authentication failed");
            return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
        }
    }

}