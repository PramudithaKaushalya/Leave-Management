package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.payload.ApiResponse;
import com.example.demo.payload.LeaveCountDTO;
import com.example.demo.payload.LeaveProfileDTO;
import com.example.demo.model.User;
import com.example.demo.model.LeaveCount;
import com.example.demo.model.LeaveType;
import com.example.demo.repository.LeaveCountRepository;
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
    
    private static final Logger LOGGER = LoggerFactory.getLogger(LeaveCountService.class);

    public ResponseEntity<?> getProfile( Long userId) {
       try { 
            List<LeaveCount> all = leaveCountRepository.findAll();
        
            List<User> currentEmployees = userRepository.findByStatus("Working");

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
                                profile.setMaternity(count); 
                                break;
                            case 4:
                                profile.setPaternity(count);
                                break;
                            case 5: 
                                profile.setAnnual(count);
                                break;
                            case 6:
                                profile.setLieu(count);
                                break;
                            case 7:
                                profile.setSpecial(count);
                                break;
                            case 8:
                                profile.setCoverup(count);
                                break;
                            case 9:
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
            return ResponseEntity.ok(new ApiResponse(false, "Unable to get all leave count profiles"));
        }
    }

    public ResponseEntity<?> getOneProfile(Long id, Long userId) {

        try {
            User emp = userRepository.getOne(id);
            LeaveProfileDTO oneProfile = new LeaveProfileDTO();

            LeaveType casual = new LeaveType();
            casual.setLeave_type_id(1);
            if(leaveCountRepository.existsByUserAndType(emp, casual)){
                LeaveCount casual_count_object = leaveCountRepository.findByUserAndType(emp, casual);
                Float casual_count = casual_count_object.getCount();
                oneProfile.setCasual(casual_count);
            }else{
                oneProfile.setCasual(0.0F);
            }   

            LeaveType medical = new LeaveType();
            medical.setLeave_type_id(2);
            if(leaveCountRepository.existsByUserAndType(emp, medical)){
                LeaveCount medical_count_object = leaveCountRepository.findByUserAndType(emp, medical);
                Float medical_count = medical_count_object.getCount();
                oneProfile.setMedical(medical_count);
            }else{
                oneProfile.setMedical(0.0F);
            }

            LeaveType maternity = new LeaveType();
            maternity.setLeave_type_id(3);
            if(leaveCountRepository.existsByUserAndType(emp, maternity)){
                LeaveCount count_object = leaveCountRepository.findByUserAndType(emp, maternity);
                Float count = count_object.getCount();
                oneProfile.setMaternity(count);;
            }else{
                oneProfile.setMaternity(0.0F);
            }

            LeaveType paternity = new LeaveType();
            paternity.setLeave_type_id(4);
            if(leaveCountRepository.existsByUserAndType(emp, paternity)){
                LeaveCount count_object = leaveCountRepository.findByUserAndType(emp, paternity);
                Float count = count_object.getCount();
                oneProfile.setPaternity(count);
            }else{
                oneProfile.setPaternity(0.0F);
            }

            LeaveType annual = new LeaveType();
            annual.setLeave_type_id(5);
            if(leaveCountRepository.existsByUserAndType(emp, annual)){
                LeaveCount annual_count_object = leaveCountRepository.findByUserAndType(emp, annual);
                Float annual_count = annual_count_object.getCount();
                oneProfile.setAnnual(annual_count);
            }else{
                oneProfile.setAnnual(0.0F);
            }
            
            LOGGER.info(">>> Successfully get leave count profile of user "+id+". (By user ==> "+userId+")");
            return ResponseEntity.ok(new ApiResponse(true, oneProfile));
        } catch(Exception e) {
            LOGGER.error(">>> Unable to get leave count profile of user "+id+". (By user ==> "+userId+")", e.getMessage());
            return ResponseEntity.ok(new ApiResponse(false, "Unable to get leave count profile"));
        }
    }

    public ResponseEntity<?> summery(Long id, Long userId){

        try {
            User employee = userRepository.getOne(id);
            List<LeaveCountDTO> summeryAll = new ArrayList<LeaveCountDTO>();
            
            LeaveCountDTO summeryCasual = new LeaveCountDTO();
            summeryCasual.setType("Casual");
            summeryCasual.setEntitlement(employee.getCasual());
            LeaveType casual = new LeaveType();
            casual.setLeave_type_id(1);
            if(leaveCountRepository.existsByUserAndType(employee, casual)){
                LeaveCount count_object = leaveCountRepository.findByUserAndType(employee, casual);
                Float count = count_object.getCount();
                summeryCasual.setUtilized(count);
            }else{
                summeryCasual.setUtilized(0.0F);
            }
            summeryCasual.setRemaining(summeryCasual.getEntitlement()-summeryCasual.getUtilized());
            summeryAll.add(summeryCasual);
            
            LeaveCountDTO summeryMedical = new LeaveCountDTO();
            summeryMedical.setType("Medical");
            summeryMedical.setEntitlement(employee.getMedical());
            LeaveType medical = new LeaveType();
            medical.setLeave_type_id(2);
            if(leaveCountRepository.existsByUserAndType(employee, medical)){
                LeaveCount count_object = leaveCountRepository.findByUserAndType(employee, medical);
                Float count = count_object.getCount();
                summeryMedical.setUtilized(count);
            }else{
                summeryMedical.setUtilized(0.0F);
            }
            summeryMedical.setRemaining(summeryMedical.getEntitlement()-summeryMedical.getUtilized());
            summeryAll.add(summeryMedical);

            LeaveCountDTO summeryAnnual = new LeaveCountDTO();
            summeryAnnual.setType("Annual");
            summeryAnnual.setEntitlement(employee.getAnnual());
            LeaveType annual = new LeaveType();
            annual.setLeave_type_id(5);
            if(leaveCountRepository.existsByUserAndType(employee, annual)){
                LeaveCount count_object = leaveCountRepository.findByUserAndType(employee, annual);
                Float count = count_object.getCount();
                summeryAnnual.setUtilized(count);;
            }else{
                summeryAnnual.setUtilized(0.0F);;
            }
            summeryAnnual.setRemaining(summeryAnnual.getEntitlement()-summeryAnnual.getUtilized());
            summeryAll.add(summeryAnnual);

            if(leaveCountRepository.existsByUser(employee)){
                List<LeaveCount> otherTypes = leaveCountRepository.findByUser(employee);

                for (LeaveCount leaveCount : otherTypes) {
                    LeaveCountDTO summeryType = new LeaveCountDTO();
                    LeaveType leave_type = leaveCount.getType();
                    Integer type = leave_type.getLeave_type_id();
                    summeryType.setUtilized(leaveCount.getCount()); 
                    summeryType.setEntitlement(0.0F);
                    summeryType.setRemaining(0.0F);

                    switch (type) {
                        case 3:
                            summeryType.setType("Maternity");
                            summeryAll.add(summeryType);
                            break;
                        case 4:
                            summeryType.setType("Paternity");
                            summeryAll.add(summeryType);
                            break;
                        case 6:
                            summeryType.setType("Lieu");
                            summeryAll.add(summeryType);
                            break;
                        case 7:
                            summeryType.setType("Special");
                            summeryAll.add(summeryType);
                            break;
                        case 8:
                            summeryType.setType("Cover Up");
                            summeryAll.add(summeryType);
                            break;
                        case 9:
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
            return ResponseEntity.ok(new ApiResponse(false, "Unable to get leave summery"));
        }
    }

}    