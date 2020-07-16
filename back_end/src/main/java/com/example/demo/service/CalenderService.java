package com.example.demo.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.model.Calender;
import com.example.demo.model.User;
import com.example.demo.payload.ApiResponse;
import com.example.demo.payload.CalenderRecord;
import com.example.demo.repository.CalenderRepository;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CalenderService {

    @Autowired
    private CalenderRepository calenderRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(CalenderService.class);

    public ResponseEntity<?> saveDate(Calender date, User user) {
        try {
            LocalDateTime datetime1 = LocalDateTime.now();  
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");  
            String createdAt = datetime1.format(format);

            date.setCreatedAt(createdAt);
            date.setCreatedBy(user);

            calenderRepository.save(date);

            LOGGER.info(">>> Successfully save the holiday "+date.getDate()+". (By user ==> "+user.getId()+")");
            return ResponseEntity.ok(new ApiResponse(true, "Successfully save the holiday"));
        }catch(Exception e){
            LOGGER.error(">>> Unable to change the state of user. (By User ==> "+user.getId()+")", e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(false, "Unable to save the holiday"));
        }
    }

    public ResponseEntity<?> getAll( Long userId) {

        try{
            List<Calender> all = calenderRepository.findAll();

            List<CalenderRecord> records = all.stream().map(var -> new CalenderRecord(var.getId(), var.getDate(), var.getReason(), var.getCreatedBy().getFirstName()+" "+var.getCreatedBy().getSecondName(), var.getCreatedAt()) ).collect(Collectors.toList());

            LOGGER.info(">>> Successfully get all calender records. (By user ==> "+userId+")");

            return ResponseEntity.ok(new ApiResponse(true, records));
        } catch(Exception e) {
            LOGGER.error(">>> Unable to get all calender records. (By user ==> "+userId+")", e.getMessage());
            return ResponseEntity.ok(new ApiResponse(false, "Unable to get all calender records"));

        }

    }

    public ResponseEntity<?> deleteDate(Long id, Long userId) {
        try {
            calenderRepository.deleteById(id);
            LOGGER.info(">>> Successfully delete calender record "+id+". (By user ==> "+userId+")");
            return ResponseEntity.ok(new ApiResponse(true, "Remove Holiday successfull"));
        } catch(Exception e) {
            LOGGER.error(">>> Unable to delete calender record "+id+". (By user ==> "+userId+")", e.getMessage());
            return ResponseEntity.ok(new ApiResponse(false, "Unable to get all calender records"));
        }

    }

    public ResponseEntity<?> getDates( Long userId ) {
        try {

            List<Calender> all = calenderRepository.findAll();

            List<String> dates = all.stream().map(var -> var.getDate()).collect(Collectors.toList());

            LOGGER.info(">>> Successfully get dates. (By user ==> "+userId+")");
            return ResponseEntity.ok(new ApiResponse(true, dates));
        } catch(Exception e) {
            LOGGER.error(">>> Unable to get dates. (By user ==> "+userId+")", e.getMessage());
            return ResponseEntity.ok(new ApiResponse(false, "Unable to get all calender records"));
        }
    }
}