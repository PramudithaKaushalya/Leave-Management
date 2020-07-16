package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.payload.ApiResponse;
import com.example.demo.repository.UserRepository;
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

    @Autowired
    UserRepository userRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(LeaveTypeController.class);
    
    @GetMapping("/all")
    public ResponseEntity<?> getAll(@RequestHeader("Authorization") String token) {
        if(StringUtils.hasText(token) && token.startsWith("Bearer ")){
            String jwt = token.substring(7);
            Long id = tokenProvider.getUserIdFromJWT(jwt);

            User user = userRepository.getOne(id);
            Long role_id = user.getRoles().stream().findFirst().get().getId();

            if(role_id == 1L || role_id == 2L || role_id == 6L){
                return leaveTypeService.getAll(id);
            }else{
                return leaveTypeService.getSome(id);
            }
        } else {
            LOGGER.warn(">>> User authentication failed");
            return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
        }
    }
}