package com.example.demo.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import com.example.demo.model.LieuLeave;
import com.example.demo.model.User;
import com.example.demo.payload.ApiResponse;
import com.example.demo.payload.LieuLeaveDTO;
import com.example.demo.repository.LieuLeaveRepository;
import com.example.demo.repository.UserRepository;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LieuLeaveService {

    @Autowired
    private LieuLeaveRepository lieuLeaveRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(LieuLeaveService.class);

    public ResponseEntity<?> addLieuLeave(LieuLeave lieuLeave, Long userId) {
        try {
            Date date = new Date();
            User user = userRepository.getOne(userId);
            lieuLeave.setEmployee(user);
            lieuLeave.setIsApproved(false);
            lieuLeave.setRequestAt(date);
            lieuLeaveRepository.save(lieuLeave);
            LOGGER.info(">>> Successfully add the lieu leave. (By user ==> "+userId+")");
            return ResponseEntity.ok(new ApiResponse(true, "Successfully requested to collect lieu leave"));
        } catch(Exception e) {
            LOGGER.error(">>> Unable to add lieu leave. (By user ==> "+userId+")");
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(false, "Unable to add contact"));
        }
    }

    public ResponseEntity<?> getAllLieuLeaves(Long userId) {
        try {
            List<LieuLeave> leaves = lieuLeaveRepository.findByIsApprovedOrderByIdDesc(false);
            List<LieuLeaveDTO> leaveDTOs = leaves.stream().map( x -> new LieuLeaveDTO(x.getId(), x.getDate(), x.getPeriod(), x.getProject(), x.getWorksDone(), x.getEmployee().getFirstName()+" "+ x.getEmployee().getSecondName())).collect(Collectors.toList());
            LOGGER.info(">>> Successfully get all lieu leaves. (By user ==> "+userId+")");
            return ResponseEntity.ok(new ApiResponse(true, leaveDTOs));
        } catch(Exception e) {
            LOGGER.error(">>> Unable to get all lieu leaves. (By user ==> "+userId+")");
            return ResponseEntity.ok(new ApiResponse(false, "Unable to get all lieu leaves"));
        }
    }

    public ResponseEntity<?> getOwnLieuLeaves(Long userId) {
        try {
            User user = userRepository.getOne(userId);

            if(lieuLeaveRepository.existsByEmployee(user)) {
                List<LieuLeave> leaves = lieuLeaveRepository.findByEmployeeOrderByIdDesc(user);
                List<LieuLeaveDTO> leaveDTOs = leaves.stream().map( x -> new LieuLeaveDTO(x.getDate(), x.getPeriod(), x.getProject(), x.getIsApproved()) ).collect(Collectors.toList());
            
                LOGGER.info(">>> Successfully get all lieu leaves. (By user ==> "+userId+")");
                return ResponseEntity.ok(new ApiResponse(true, leaveDTOs));
            } else {
                LOGGER.info(">>> There are no lieu leaves. (By user ==> "+userId+")");
                return ResponseEntity.ok(new ApiResponse(true, new ArrayList<>()));
            }
        } catch(Exception e) {
            LOGGER.error(">>> Unable to get all lieu leaves. (By user ==> "+userId+")");
            return ResponseEntity.ok(new ApiResponse(false, "Unable to get all lieu leaves"));
        }
    }

    public ResponseEntity<?> approveLieuLeaves(Long leaveId, Long userId) {
        try {
            Date date = new Date();

            LieuLeave leave = lieuLeaveRepository.getOne(leaveId);
            leave.setApprovedBy(userRepository.getOne(userId));
            leave.setIsApproved(true);
            leave.setApprovedAt(date);
            lieuLeaveRepository.save(leave);
            LOGGER.info(">>> Successfully approved the lieu leave. (By user ==> "+userId+")");
            return ResponseEntity.ok(new ApiResponse(true, "Successfully approved the lieu leave"));
        } catch(Exception e) {
            LOGGER.error(">>> Unable to approved the lieu leave. (By user ==> "+userId+")");
            return ResponseEntity.ok(new ApiResponse(false, "Unable to approved the lieu leave"));
        }
    }
}