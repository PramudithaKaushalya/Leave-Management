package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import com.example.demo.dto.LeaveProfileDTO;
import com.example.demo.model.Employee;
import com.example.demo.model.LeaveCount;
import com.example.demo.model.LeaveType;
import com.example.demo.repository.LeaveCountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LeaveCountService {

    @Autowired
    private LeaveCountRepository leaveCountRepository;

    public List<LeaveCount> getAll() {
        System.out.println("Get All Leave Counts of Employees !!!");
        return leaveCountRepository.findAll();
    }

    public void addEmpty() {
        LeaveCount add = new LeaveCount();
        leaveCountRepository.save(add);
    }

    public List<LeaveProfileDTO> getProfile() {
        List<LeaveCount> all = leaveCountRepository.findAll();
        
        List<LeaveProfileDTO> profiles = new ArrayList<LeaveProfileDTO>();
        List<Employee> employees = new ArrayList<Employee>();

        for (LeaveCount leaveCount : all) {

            Employee emp = leaveCount.getEmployee();
            Integer emp_id = emp.getEmp_id();
            String name = emp.getFirst_name() + ' ' + emp.getSecond_name();

            LeaveType leave_type = leaveCount.getType();
            Integer type = leave_type.getLeave_type_id();

            Float count = leaveCount.getCount();

            if(employees.contains(emp)){
                Integer i = employees.indexOf(emp);
                LeaveProfileDTO profile = profiles.get(i);

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

            }else{

                LeaveProfileDTO profile = new LeaveProfileDTO(emp_id,name,0.0F,0.0F,0.0F,0.0F,0.0F,0.0F,0.0F,0.0F,0.0F);

                profile.setId(emp_id);
                profile.setName(name);

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

                profiles.add(profile);
                employees.add(emp);
            }

        }
        return profiles;
    }

    public LeaveProfileDTO getOneProfile(Employee emp) {

        LeaveProfileDTO oneProfile = new LeaveProfileDTO();

        LeaveType casual = new LeaveType();
        casual.setLeave_type_id(1);
        if(leaveCountRepository.existsByEmployeeAndType(emp, casual)){
            LeaveCount casual_count_object = leaveCountRepository.findByEmployeeAndType(emp, casual);
            Float casual_count = casual_count_object.getCount();
            oneProfile.setCasual(casual_count);
        }else{
            oneProfile.setCasual(0.0F);
        }   

        LeaveType medical = new LeaveType();
        medical.setLeave_type_id(2);
        if(leaveCountRepository.existsByEmployeeAndType(emp, medical)){
            LeaveCount medical_count_object = leaveCountRepository.findByEmployeeAndType(emp, medical);
            Float medical_count = medical_count_object.getCount();
            oneProfile.setMedical(medical_count);
        }else{
            oneProfile.setMedical(0.0F);
        }

        LeaveType annual = new LeaveType();
        annual.setLeave_type_id(5);
        if(leaveCountRepository.existsByEmployeeAndType(emp, annual)){
            LeaveCount annual_count_object = leaveCountRepository.findByEmployeeAndType(emp, annual);
            Float annual_count = annual_count_object.getCount();
            oneProfile.setAnnual(annual_count);
        }else{
            oneProfile.setAnnual(0.0F);
        }

        return oneProfile;
    }
}    