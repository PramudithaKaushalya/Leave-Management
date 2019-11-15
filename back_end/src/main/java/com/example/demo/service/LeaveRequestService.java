package com.example.demo.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.example.demo.model.Employee;
import com.example.demo.model.LeaveCount;
import com.example.demo.model.LeaveRequest;
import com.example.demo.model.LeaveType;
import com.example.demo.repository.LeaveCountRepository;
import com.example.demo.repository.LeaveRequestRepository;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class LeaveRequestService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private LeaveCountRepository leaveCountRepository;

    @Autowired
    private JavaMailSender javaMailSender;
    
	private static final Logger LOGGER = LoggerFactory.getLogger(LeaveRequestService.class);

    public List<LeaveRequest> getAll() {
        System.out.println("Get All Requests !!!");
        return leaveRequestRepository.findAll();
    }

    public List<LeaveRequest> getOneEmployeeLeaveRequests(Employee employee) {
        return leaveRequestRepository.findByEmployee(employee);
    }

    public String addRequest(LeaveRequest leaveRequest) {
        LocalDateTime datetime1 = LocalDateTime.now();  
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");  
        String formatDateTime = datetime1.format(format);
        
        leaveRequest.setFormatDateTime(formatDateTime);

        leaveRequestRepository.save(leaveRequest);
        return "Request the leave !!!";
    }

    public List<LeaveRequest> getPending(String pending) {
        System.out.println("Get All Pending Requests !!!");
        return leaveRequestRepository.findByStatus(pending);
    }

    public String deleteLeave(Integer id) {
        leaveRequestRepository.deleteById(id);
        return "Remove Leave Request!!!";
    }

    public String approveRequest(LeaveRequest leaveRequest) {
        Integer id = leaveRequest.getLeave_id();
        LeaveRequest request = leaveRequestRepository.getOne(id);
        request.setStatus("Approved");
        leaveRequestRepository.save(request);

        Employee emp = request.getEmployee();
        LeaveType type = request.getLeave_type();
        Float days = request.getNumber_of_leave_days();

        if(leaveCountRepository.existsByEmployee(emp) && leaveCountRepository.existsByType(type)) {
            
            LeaveCount filter = leaveCountRepository.findByEmployeeAndType(emp, type);
            filter.setCount(filter.getCount()+days);
            leaveCountRepository.save(filter);
        }else{
            LeaveCount count = new LeaveCount(emp, type, days);
            leaveCountRepository.save(count);
        }
        acceptRequest(leaveRequest);
        mailDutyCover(leaveRequest.getDuty() , leaveRequest);
        LOGGER.info(">>> Accept Leave Request");
        return "Accept Leave Request!!!";
    }

    private void mailDutyCover (Employee duty, LeaveRequest leave ) {

        Employee employee = leave.getEmployee();
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(duty.getEmail());
		msg.setSubject("Aditional duty to cover.");
		msg.setText("Hi "+duty.getFirst_name()+" "+duty.getSecond_name()+",\n"+
					"You are asigned to cover the duty of following employee in these day/s. \n\n"+
					"Name: "+employee.getFirst_name()+" "+employee.getSecond_name()+"\n"+
					"Role: "+employee.getRole().getRole_name()+"\n"+
                    "Department: "+employee.getDepartment().getDepartment_name()+"\n"+
                    "Date/s: "+leave.getStart_date()+" to "+leave.getEnd_date()+"\n\n"+
					"Thanks. \n Best Regards"
					);

		try {
			javaMailSender.send(msg);
			LOGGER.info(">>> E-mail send to ==> "+duty.getEmail());
		}catch (Exception e){
			LOGGER.error(">>> (MailSender) ==> "+e);
		}
    }

    private void acceptRequest (LeaveRequest leave ) {

        Employee employee = leave.getEmployee();
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(employee.getEmail());
		msg.setSubject("Aditional duty to cover.");
		msg.setText("Hi "+employee.getFirst_name()+" "+employee.getSecond_name()+",\n"+
					"Accept your leave request. \n\n"+
					"Thanks. \n Best Regards"
					);

		try {
			javaMailSender.send(msg);
			LOGGER.info(">>> E-mail send to ==> "+employee.getEmail());
		}catch (Exception e){
			LOGGER.error(">>> (MailSender) ==> "+e);
		}
    }
    
    // public List<Contact> getOne(Employee employee) {
    //     System.out.println("Get contacts of one employee !!!");
    //     return contactRepository.findByEmployee(employee);
    // }
}