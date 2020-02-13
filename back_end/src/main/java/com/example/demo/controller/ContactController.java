package com.example.demo.controller;

import com.example.demo.model.Contact;
import com.example.demo.model.User;
import com.example.demo.payload.ApiResponse;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.ContactService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;    
    
    @Autowired
    JwtTokenProvider tokenProvider;

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactController.class);

    @PostMapping(value = "/add")
    public ResponseEntity<?> addEmployee(@RequestHeader("Authorization") String token, @RequestBody Contact contact) {
        if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {

            String jwt = token.substring(7);
            Long userId = tokenProvider.getUserIdFromJWT(jwt);
            return contactService.addContact(contact, userId);
        }
        else {
            LOGGER.warn(">>> User authentication failed");
            return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
        }
    }

    @PostMapping(value = "/employee")
    public ResponseEntity<?> getOne(@RequestHeader("Authorization") String token, @RequestBody User employee) {
        if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {

            String jwt = token.substring(7);
            Long userId = tokenProvider.getUserIdFromJWT(jwt);
            return contactService.getOne(employee, userId);
        }
        else {
            LOGGER.warn(">>> User authentication failed");
            return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
        }
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> DeleteById(@RequestHeader("Authorization") String token, @PathVariable("id") Integer contact) {
        if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {

            String jwt = token.substring(7);
            Long userId = tokenProvider.getUserIdFromJWT(jwt);
            return contactService.deleteContact(contact, userId);
        }
        else {
            LOGGER.warn(">>> User authentication failed");
            return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
        }
    }
}