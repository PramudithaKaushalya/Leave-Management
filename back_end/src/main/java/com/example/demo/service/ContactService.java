package com.example.demo.service;

import com.example.demo.model.Contact;
import com.example.demo.model.User;
import com.example.demo.payload.ApiResponse;
import com.example.demo.repository.ContactRepository;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactService.class);

    public ResponseEntity<?> getOne(User user, Long userId) {
        try {
            LOGGER.info(">>> Successfully get contact records of "+user.getId()+". (By user ==> "+userId+")");
            return ResponseEntity.ok(new ApiResponse(true, contactRepository.findByUser(user)));
        } catch(Exception e) {
            LOGGER.error(">>> Unable to get contact records of "+user.getId()+". (By user ==> "+userId+")", e.getMessage());
            return ResponseEntity.ok(new ApiResponse(false, "Unable to get contact records"));
        }
    }

    public ResponseEntity<?> addContact(Contact contact, Long userId) {
        try {
            contactRepository.save(contact);
            LOGGER.info(">>> Successfully add contact records of "+contact.getUser().getId()+". (By user ==> "+userId+")");
            return ResponseEntity.ok(new ApiResponse(true, "Successfully add contact"));
        } catch(Exception e) {
            LOGGER.error(">>> Unable to add contact records of "+contact.getUser().getId()+". (By user ==> "+userId+")", e.getMessage());
            return ResponseEntity.ok(new ApiResponse(false, "Unable to add contact"));
        }
    }

    public ResponseEntity<?> deleteContact(Integer contact, Long userId) {
        try {
            contactRepository.deleteById(contact);
            LOGGER.info(">>> Successfully delete contact records of "+contact+". (By user ==> "+userId+")");
            return ResponseEntity.ok(new ApiResponse(true, "Successfully delete contact"));
        } catch(Exception e) {
            LOGGER.error(">>> Unable to delete contact records of "+contact+". (By user ==> "+userId+")", e.getMessage());
            return ResponseEntity.ok(new ApiResponse(false, "Unable to delete contact"));
        }
    }
}