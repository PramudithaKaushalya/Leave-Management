package com.example.demo.controller;

import com.example.demo.model.Calender;
import com.example.demo.model.User;
import com.example.demo.payload.ApiResponse;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.CalenderService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/calender")
public class CalenderController {

    @Autowired
    private  CalenderService calenderService;
    
    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    UserRepository userRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(CalenderController.class);

    @GetMapping("/all")
    public ResponseEntity<?> getAll(@RequestHeader("Authorization") String token) {
        if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {
			String jwt = token.substring(7);
			Long userId = tokenProvider.getUserIdFromJWT(jwt);

            return calenderService.getAll(userId);
        }
        else {
            LOGGER.warn(">>> User authentication failed");
            return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
        }
    }

    @PostMapping("/set_date")
    public ResponseEntity<?> setDate(@RequestHeader("Authorization") String token, @RequestBody Calender calender){
        if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {

            String jwt = token.substring(7);
            Long id = tokenProvider.getUserIdFromJWT(jwt);
            User user = userRepository.getOne(id);
            return calenderService.saveDate(calender, user);
        }
        else {
            LOGGER.warn(">>> User authentication failed");
            return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
        }
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@RequestHeader("Authorization") String token, @PathVariable("id") Long id) {
        if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {

            String jwt = token.substring(7);
            Long userId = tokenProvider.getUserIdFromJWT(jwt);
            return calenderService.deleteDate(id, userId);
        }
        else {
            LOGGER.warn(">>> User authentication failed");
            return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
        }
    }

    @GetMapping("/dates")
    public ResponseEntity<?> getDates(@RequestHeader("Authorization") String token) {
        if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {

            String jwt = token.substring(7);
            Long userId = tokenProvider.getUserIdFromJWT(jwt);
            return calenderService.getDates(userId);
        }
        else {
            LOGGER.warn(">>> User authentication failed");
            return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
        }
    }
}