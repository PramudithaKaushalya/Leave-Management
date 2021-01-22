package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.payload.ApiResponse;
import com.example.demo.payload.LeaveCountDTO;
import com.example.demo.payload.LeaveProfileDTO;
import com.example.demo.model.User;
import com.example.demo.model.LeaveCount;
import com.example.demo.model.LeaveType;
import com.example.demo.model.LieuLeave;
import com.example.demo.repository.LeaveCountRepository;
import com.example.demo.repository.LieuLeaveRepository;
import com.example.demo.repository.UserRepository;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LeaveCountService {

    @Autowired
    private LeaveCountRepository leaveCountRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private LieuLeaveRepository lieuLeaveRepository;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(LeaveCountService.class);

    public ResponseEntity<?> getProfile( Long userId) {
       try {
            LocalDateTime nowTime = LocalDateTime.now();
            List<LeaveCount> all = leaveCountRepository.findByYear(nowTime.getYear());
        
            List<User> currentEmployees = userRepository.findByStatusOrderByFirstName("Working");

            List<LeaveProfileDTO> profiles = currentEmployees.stream().map(user -> new LeaveProfileDTO(user.getId(),user.getFirstName()+" "+user.getSecondName(),0.0F,0.0F,0.0F,0.0F,0.0F,0.0F,0.0F,0.0F,0.0F)).collect(Collectors.toList());

            for (LeaveCount leaveCount : all) {

                User emp = leaveCount.getUser();

                LeaveType leave_type = leaveCount.getType();
                Integer type = leave_type.getLeave_type_id();

                Float count = leaveCount.getCount();

                for (LeaveProfileDTO profile : profiles) {
                    if(profile.getId() == emp.getId()) {
                        switch (type) {
                            case 1: 
                                profile.setCasual(count);
                                break;
                            case 2:
                                profile.setMedical(count);
                                break;
                            case 3:
                                profile.setAnnual(count);
                                break;
                            case 4:
                                profile.setLieu(count);
                                break;
                            case 5:
                                profile.setSpecial(count);
                                break;
                            case 6:
                                profile.setMaternity(count);
                                break;
                            case 7:
                                profile.setPaternity(count);
                                break;
                            case 8:
                                profile.setNopay(count);
                                break;
                            default:
                                break;         
                        }
                    }       
                }

                }
            LOGGER.info(">>> Successfully get all leave count profiles. (By user ==> "+userId+")");

            return ResponseEntity.ok(new ApiResponse(true, profiles));
        } catch(Exception e) {
            LOGGER.error(">>> Unable to get all leave count profiles. (By user ==> "+userId+")", e.getMessage());
           e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(false, "Unable to get all leave count profiles"));
        }
    }

    public ResponseEntity<?> getOneProfile(Long id, Long userId) {

        try {
            LocalDateTime nowTime = LocalDateTime.now();
            User emp = userRepository.getOne(id);
            LeaveProfileDTO oneProfile = new LeaveProfileDTO();

            LeaveType casual = new LeaveType();
            casual.setLeave_type_id(1);
            if(leaveCountRepository.existsByUserAndTypeAndYear(emp, casual, nowTime.getYear())){
                LeaveCount casual_count_object = leaveCountRepository.findByUserAndTypeAndYear(emp, casual, nowTime.getYear());
                Float casual_count = casual_count_object.getCount();
                oneProfile.setCasual(casual_count);
            }else{
                oneProfile.setCasual(0.0F);
            }   

            LeaveType medical = new LeaveType();
            medical.setLeave_type_id(2);
            if(leaveCountRepository.existsByUserAndTypeAndYear(emp, medical, nowTime.getYear())){
                LeaveCount medical_count_object = leaveCountRepository.findByUserAndTypeAndYear(emp, medical, nowTime.getYear());
                Float medical_count = medical_count_object.getCount();
                oneProfile.setMedical(medical_count);
            }else{
                oneProfile.setMedical(0.0F);
            }

            LeaveType maternity = new LeaveType();
            maternity.setLeave_type_id(6);
            if(leaveCountRepository.existsByUserAndTypeAndYear(emp, maternity, nowTime.getYear())){
                LeaveCount count_object = leaveCountRepository.findByUserAndTypeAndYear(emp, maternity, nowTime.getYear());
                Float count = count_object.getCount();
                oneProfile.setMaternity(count);;
            }else{
                oneProfile.setMaternity(0.0F);
            }

            LeaveType paternity = new LeaveType();
            paternity.setLeave_type_id(7);
            if(leaveCountRepository.existsByUserAndTypeAndYear(emp, paternity, nowTime.getYear())){
                LeaveCount count_object = leaveCountRepository.findByUserAndTypeAndYear(emp, paternity, nowTime.getYear());
                Float count = count_object.getCount();
                oneProfile.setPaternity(count);
            }else{
                oneProfile.setPaternity(0.0F);
            }

            LeaveType annual = new LeaveType();
            annual.setLeave_type_id(3);
            if(leaveCountRepository.existsByUserAndTypeAndYear(emp, annual, nowTime.getYear())){
                LeaveCount annual_count_object = leaveCountRepository.findByUserAndTypeAndYear(emp, annual, nowTime.getYear());
                Float annual_count = annual_count_object.getCount();
                oneProfile.setAnnual(annual_count);
            }else{
                oneProfile.setAnnual(0.0F);
            }
            
            LOGGER.info(">>> Successfully get leave count profile of user "+id+". (By user ==> "+userId+")");
            return ResponseEntity.ok(new ApiResponse(true, oneProfile));
        } catch(Exception e) {
            LOGGER.error(">>> Unable to get leave count profile of user "+id+". (By user ==> "+userId+")", e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(false, "Unable to get leave count profile"));
        }
    }

    public ResponseEntity<?> summery(Long id, Long userId){

        try {
            LocalDateTime nowTime = LocalDateTime.now();
            User employee = userRepository.getOne(id);
            List<LeaveCountDTO> summeryAll = new ArrayList<LeaveCountDTO>();

            LeaveCountDTO casual = getCountsOfLeaveTypes("Casual", employee.getCasual(), 1, employee);
            summeryAll.add(casual);

            LeaveCountDTO medical = getCountsOfLeaveTypes("Medical", employee.getMedical(), 2, employee);
            summeryAll.add(medical);

            LeaveCountDTO annual = getCountsOfLeaveTypes("Annual", employee.getAnnual(), 3, employee);
            summeryAll.add(annual);

            LeaveCountDTO lieu = getCountsOfLieu("Lieu", getLieuCount(employee), 4, employee);
            summeryAll.add(lieu);

            if(leaveCountRepository.existsByUserAndYear(employee, nowTime.getYear())){
                List<LeaveCount> otherTypes = leaveCountRepository.findByUserAndYear(employee, nowTime.getYear());

                for (LeaveCount leaveCount : otherTypes) {
                    LeaveCountDTO summeryType = new LeaveCountDTO();
                    LeaveType leave_type = leaveCount.getType();
                    Integer type = leave_type.getLeave_type_id();
                    summeryType.setUtilized(leaveCount.getCount()); 
                    summeryType.setEntitlement(0.0F);
                    summeryType.setRemaining(0.0F);

                    switch (type) {
                        case 6:
                            summeryType.setType("Maternity");
                            summeryAll.add(summeryType);
                            break;
                        case 7:
                            summeryType.setType("Paternity");
                            summeryAll.add(summeryType);
                            break;
                        case 5:
                            summeryType.setType("Special");
                            summeryAll.add(summeryType);
                            break;
                        case 8:
                            summeryType.setType("No Pay");
                            summeryAll.add(summeryType);
                            break;
                        default:
                            break;         
                    }
                }
            }
            
            
            LOGGER.info(">>> Successfully get leave summery of user "+id+". (By user ==> "+userId+")");
            return ResponseEntity.ok(new ApiResponse(true, summeryAll));
        } catch(Exception e) {
            LOGGER.error(">>> Unable to get leave summery of user "+id+". (By user ==> "+userId+")", e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(false, "Unable to get leave summery"));
        }
    }

    public LeaveCountDTO getCountsOfLeaveTypes(String leaveTypeName, Float entitlement, Integer leaveTypeId, User employee) {

        LocalDateTime nowTime = LocalDateTime.now();
        LeaveCountDTO summery = new LeaveCountDTO();
        summery.setType(leaveTypeName);
        summery.setEntitlement(entitlement);

        LeaveType leaveType = new LeaveType();
        leaveType.setLeave_type_id(leaveTypeId);

        if(leaveCountRepository.existsByUserAndTypeAndYear(employee, leaveType, nowTime.getYear())){
            LeaveCount count_object = leaveCountRepository.findByUserAndTypeAndYear(employee, leaveType, nowTime.getYear());
            Float count = count_object.getCount();
            summery.setUtilized(count);
        }else{
            summery.setUtilized(0.0F);
        }


        summery.setRemaining(summery.getEntitlement()-summery.getUtilized());

        return summery;
    }

    public LeaveCountDTO getCountsOfLieu(String leaveTypeName, Float entitlement, Integer leaveTypeId, User employee) {

        LeaveCountDTO summery = new LeaveCountDTO();
        summery.setType(leaveTypeName);
        summery.setEntitlement(entitlement);

        LeaveType leaveType = new LeaveType();
        leaveType.setLeave_type_id(leaveTypeId);

        if(leaveCountRepository.existsByUserAndType(employee, leaveType)){
            LeaveCount count_object = leaveCountRepository.findByUserAndType(employee, leaveType);
            Float count = count_object.getCount();
            summery.setUtilized(count);
        }else{
            summery.setUtilized(0.0F);
        }


        summery.setRemaining(summery.getEntitlement()-summery.getUtilized());

        return summery;
    }

    public Float getLieuCount(User employee){
        List<LieuLeave> lieuLeaves = lieuLeaveRepository.findByEmployeeAndStatus(employee, 1);

        Float count = 0.0F;
        for (LieuLeave leave : lieuLeaves) {
            if(leave.getPeriod() == 0) {
                count = count + 0.5F;
            }
            if(leave.getPeriod() == 1) {
                count = count + 1.0F;
            }
        }

        return count;
    }

    public ResponseEntity<?> summeryToLeaveRequestPage(Long id, Long userId){

        try {
            User employee = userRepository.getOne(id);
            List<LeaveCountDTO> summeryAll = new ArrayList<LeaveCountDTO>();

            LeaveCountDTO casual = getRemainingCountsOfLeaveTypes("Casual", employee.getCasual(), 1, employee);
            summeryAll.add(casual);

            LeaveCountDTO medical = getRemainingCountsOfLeaveTypes("Medical", employee.getMedical(), 2, employee);
            summeryAll.add(medical);

            LeaveCountDTO annual = getRemainingCountsOfLeaveTypes("Annual", employee.getAnnual(), 3, employee);
            summeryAll.add(annual);

            LeaveCountDTO lieu = getRemainingLieuCounts("Lieu", getLieuCount(employee), 4, employee);
            summeryAll.add(lieu);

            LOGGER.info(">>> Successfully get leave summery to request page of user "+id+". (By user ==> "+userId+")");
            return ResponseEntity.ok(new ApiResponse(true, summeryAll));
        } catch(Exception e) {
            LOGGER.error(">>> Unable to get leave summery to leave request page. (By user ==> "+userId+")", e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(false, "Unable to get leave summery"));
        }
    }

    public LeaveCountDTO getRemainingCountsOfLeaveTypes(String leaveTypeName, Float entitlement, Integer leaveTypeId, User employee) {

        LocalDateTime nowTime = LocalDateTime.now();
        LeaveCountDTO summery = new LeaveCountDTO();
        summery.setType(leaveTypeName);
        summery.setEntitlement(entitlement);

        LeaveType leaveType = new LeaveType();
        leaveType.setLeave_type_id(leaveTypeId);

        if(leaveCountRepository.existsByUserAndTypeAndYear(employee, leaveType, nowTime.getYear())){
            LeaveCount count_object = leaveCountRepository.findByUserAndTypeAndYear(employee, leaveType, nowTime.getYear());
            Float accepted = count_object.getCount();
            Float pending = count_object.getPending();
            summery.setUtilized(accepted);
            summery.setPending(pending);
        }else{
            summery.setUtilized(0.0F);
            summery.setPending(0.0F);
        }
        summery.setRemaining(summery.getEntitlement()-(summery.getUtilized()+summery.getPending()));

        return summery;
    }

    public LeaveCountDTO getRemainingLieuCounts(String leaveTypeName, Float entitlement, Integer leaveTypeId, User employee) {

        LeaveCountDTO summery = new LeaveCountDTO();
        summery.setType(leaveTypeName);
        summery.setEntitlement(entitlement);

        LeaveType leaveType = new LeaveType();
        leaveType.setLeave_type_id(leaveTypeId);

        if(leaveCountRepository.existsByUserAndType(employee, leaveType)){
            LeaveCount count_object = leaveCountRepository.findByUserAndType(employee, leaveType);
            Float accepted = count_object.getCount();
            Float pending = count_object.getPending();
            summery.setUtilized(accepted);
            summery.setPending(pending);
        }else{
            summery.setUtilized(0.0F);
            summery.setPending(0.0F);
        }
        summery.setRemaining(summery.getEntitlement()-(summery.getUtilized()+summery.getPending()));

        return summery;
    }
}    