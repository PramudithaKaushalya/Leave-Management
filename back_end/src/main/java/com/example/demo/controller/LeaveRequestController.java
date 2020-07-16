package com.example.demo.controller;

import com.example.demo.model.LeaveRequest;
import com.example.demo.payload.ApiResponse;
import com.example.demo.payload.LeaveFilter;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.LeaveRequestService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/leave")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestService leaveRequestService;

    @Autowired
    JwtTokenProvider tokenProvider;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(LeaveRequestController.class);
    
    @GetMapping("/all")
    public ResponseEntity<?> getAll(@RequestHeader("Authorization") String token) {
        if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {

            String jwt = token.substring(7);
            Long userId = tokenProvider.getUserIdFromJWT(jwt);
            return leaveRequestService.getAll(userId);
        }
        else {
            LOGGER.warn(">>> User authentication failed");
            return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
        }
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> getOneEmployeeLeaveRequests(@RequestHeader("Authorization") String token, @PathVariable("id") Long employeeId) {        
        if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {

            String jwt = token.substring(7);
            Long userId = tokenProvider.getUserIdFromJWT(jwt);
            return leaveRequestService.getOneEmployeeLeaveRequests(employeeId, userId);
        }
        else {
            LOGGER.warn(">>> User authentication failed");
            return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
        }
    }

    @PostMapping(value = "/request")
    public ResponseEntity<?> addRequest(@RequestHeader("Authorization") String token, @RequestBody LeaveRequest leaveRequest) {
        if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            Long userId = tokenProvider.getUserIdFromJWT(jwt);
            return leaveRequestService.addRequest(leaveRequest, userId);
        }
        else {
            LOGGER.warn(">>> User authentication failed");
            return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
        }
    }

    @GetMapping("/pending")
    public ResponseEntity<?> getPending(@RequestHeader("Authorization") String token) {
        if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            Long userId = tokenProvider.getUserIdFromJWT(jwt);
            String pending = "Pending";
            return leaveRequestService.getPending(pending, userId);
        }
        else {
            LOGGER.warn(">>> User authentication failed");
            return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
        }
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> DeleteById(@RequestHeader("Authorization") String token, @PathVariable("id") LeaveRequest request) {
        if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            Long userId = tokenProvider.getUserIdFromJWT(jwt);
            Integer id = request.getLeave_id();
            return leaveRequestService.deleteLeave(id, userId);
        }
        else {
            LOGGER.warn(">>> User authentication failed");
            return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
        }
    }

    @GetMapping("/approve/{id}")
    public ResponseEntity<?> ApproveById(@RequestHeader("Authorization") String token, @PathVariable("id") LeaveRequest request) {
        if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            Long userId = tokenProvider.getUserIdFromJWT(jwt);
            return leaveRequestService.approveRequest(userId, request);
        }
        else {
            LOGGER.warn(">>> User authentication failed");
            return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
        }
    }

    @PostMapping(value = "/reject/{id}")
    public ResponseEntity<?> rejectLeave( @RequestHeader("Authorization") String token, @RequestBody LeaveRequest reason, @PathVariable("id") Integer id) {
        if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            Long userId = tokenProvider.getUserIdFromJWT(jwt);
            return leaveRequestService.rejectRequest(id, reason, userId);
        }
        else {
            LOGGER.warn(">>> User authentication failed");
            return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
        }
    }

    @GetMapping("/absent")
    public ResponseEntity<?> getAbsence(@RequestHeader("Authorization") String token) {
        if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            Long userId = tokenProvider.getUserIdFromJWT(jwt);
            return leaveRequestService.getAbsence(userId);
        }
        else {
            LOGGER.warn(">>> User authentication failed");
            return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
        }
    }

    @GetMapping("/requested")
    public ResponseEntity<?> getRequested(@RequestHeader("Authorization") String token) {
        if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            Long userId = tokenProvider.getUserIdFromJWT(jwt);
            return leaveRequestService.getRequested(userId);
        }
        else {
            LOGGER.warn(">>> User authentication failed");
            return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
        }
    }

    @GetMapping("/rejected")
    public ResponseEntity<?> getRejected(@RequestHeader("Authorization") String token) {
        if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            Long userId = tokenProvider.getUserIdFromJWT(jwt);
            return leaveRequestService.getRejected(userId);
        }
        else {
            LOGGER.warn(">>> User authentication failed");
            return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
        }
    }

    @PostMapping("/filter")
    public ResponseEntity<?> getFilterd(@RequestHeader("Authorization") String token, @RequestBody LeaveFilter filters) {
        if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            Long userId = tokenProvider.getUserIdFromJWT(jwt);
            return leaveRequestService.getFilterd(userId, filters);
        }
        else {
            LOGGER.warn(">>> User authentication failed");
            return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
        }
    }
}