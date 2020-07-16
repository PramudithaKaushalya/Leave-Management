package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import com.example.demo.model.LeaveType;
import com.example.demo.payload.ApiResponse;
import com.example.demo.repository.LeaveTypeRepository;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LeaveTypeService {

    @Autowired
    private LeaveTypeRepository leaveTypeRepository;

	private static final Logger LOGGER = LoggerFactory.getLogger(LeaveTypeService.class);

    public ResponseEntity<?> getAll( Long userId) {
        try {
            LOGGER.info(">>> Successfully get all leave types. (By user ==> "+userId+")");
            return ResponseEntity.ok(new ApiResponse(true, leaveTypeRepository.findAll()));
        } catch(Exception e) {
            LOGGER.error(">>> Unable to get all leave types. (By user ==> "+userId+")", e.getMessage());
            return ResponseEntity.ok(new ApiResponse(false, "Unable to get all leave types"));
        }  
    }

    public ResponseEntity<?> getSome( Long userId ) {
        try {
            List<LeaveType> leaves = new ArrayList<LeaveType>();
            
            leaves.add(leaveTypeRepository.getOne(1));
            leaves.add(leaveTypeRepository.getOne(7));
            leaves.add(leaveTypeRepository.getOne(9));

            LOGGER.info(">>> Successfully get some leave types. (By user ==> "+userId+")");
            return ResponseEntity.ok(new ApiResponse(true, leaves));
        } catch(Exception e) {
            LOGGER.error(">>> Unable to get some leave types. (By user ==> "+userId+")", e.getMessage());
            return ResponseEntity.ok(new ApiResponse(false, "Unable to get leave types"));
        } 
    }
}    