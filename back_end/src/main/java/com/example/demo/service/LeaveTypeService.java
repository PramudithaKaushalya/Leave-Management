package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import com.example.demo.model.LeaveType;
import com.example.demo.model.User;
import com.example.demo.payload.ApiResponse;
import com.example.demo.repository.LeaveTypeRepository;
import com.example.demo.repository.UserRepository;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LeaveTypeService {

    @Autowired
    private LeaveTypeRepository leaveTypeRepository;

    @Autowired
    UserRepository userRepository;

	private static final Logger LOGGER = LoggerFactory.getLogger(LeaveTypeService.class);

    public ResponseEntity<?> getAll( Long userId) {
        try {
            User user = userRepository.getOne(userId);
            Long role_id = user.getRoles().stream().findFirst().get().getId();

            if(role_id == 1L || role_id == 2L || role_id == 6L){
                if(user.getGender().equals("Male")) {
                    return ResponseEntity.ok(new ApiResponse(true, getMaleTypes(userId)));
                } else {
                    return ResponseEntity.ok(new ApiResponse(true, getFemaleTypes(userId)));
                }
            }else{
                return ResponseEntity.ok(new ApiResponse(true, getSome(userId)));
            }
        } catch(Exception e) {
            LOGGER.error(">>> Unable to get all leave types. (By user ==> "+userId+")", e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(false, "Unable to get all leave types"));
        }
    }


    public List<LeaveType> getMaleTypes( Long userId) {
        List<LeaveType> leaves = new ArrayList<>();

        leaves.add(leaveTypeRepository.getOne(1));
        leaves.add(leaveTypeRepository.getOne(2));
        leaves.add(leaveTypeRepository.getOne(3));
        leaves.add(leaveTypeRepository.getOne(4));
        leaves.add(leaveTypeRepository.getOne(5));
        leaves.add(leaveTypeRepository.getOne(7));
        leaves.add(leaveTypeRepository.getOne(8));

        LOGGER.info(">>> Successfully get male leave types. (By user ==> "+userId+")");
        return leaves;
    }

    public List<LeaveType> getFemaleTypes( Long userId) {
        List<LeaveType> leaves = new ArrayList<>();

        leaves.add(leaveTypeRepository.getOne(1));
        leaves.add(leaveTypeRepository.getOne(2));
        leaves.add(leaveTypeRepository.getOne(3));
        leaves.add(leaveTypeRepository.getOne(4));
        leaves.add(leaveTypeRepository.getOne(5));
        leaves.add(leaveTypeRepository.getOne(6));
        leaves.add(leaveTypeRepository.getOne(8));

        LOGGER.info(">>> Successfully get female leave types. (By user ==> "+userId+")");
        return leaves;
    }

    public List<LeaveType> getSome( Long userId ) {
        List<LeaveType> leaves = new ArrayList<>();

        leaves.add(leaveTypeRepository.getOne(1));
        leaves.add(leaveTypeRepository.getOne(4));
        leaves.add(leaveTypeRepository.getOne(5));
        leaves.add(leaveTypeRepository.getOne(8));

        LOGGER.info(">>> Successfully get some leave types. (By user ==> "+userId+")");
        return leaves;
    }
}    