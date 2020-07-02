package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.model.Department;
import com.example.demo.model.LeaveCount;
import com.example.demo.model.LeaveRequest;
import com.example.demo.model.LeaveType;
import com.example.demo.model.User;
import com.example.demo.payload.AbsenceUser;
import com.example.demo.payload.ApiResponse;
import com.example.demo.payload.LeaveFilter;
import com.example.demo.payload.LeaveRecord;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.LeaveCountRepository;
import com.example.demo.repository.LeaveRequestRepository;
import com.example.demo.repository.UserRepository;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    private UserRepository userRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private JavaMailSender javaMailSender;
    
	private static final Logger LOGGER = LoggerFactory.getLogger(LeaveRequestService.class);

    public ResponseEntity<?> getAll(Long userId) {
        try {
            List<LeaveRequest> all = leaveRequestRepository.findAll();
            List<LeaveRecord> requests = new ArrayList<LeaveRecord>();

            for (LeaveRequest request : all) {
                if(request.getStatus().equals("Pending") && request.getDuty()==null){
                    LeaveRecord record = new LeaveRecord( request.getLeave_id(), request.getLeave_type().getType(), request.getUser().getId(), request.getUser().getFirstName()+" "+request.getUser().getSecondName(), request.getUser().getDepartment().getName(), request.getStartDate(), 
                                                        request.getEnd_date(), request.getStartHalf(), request.getEndHalf(), request.getNumber_of_leave_days(), request.getUser().getSupervisor1(), request.getUser().getSupervisor2(), 
                                                        "No one assigned", request.getSpecial_notes(), request.getStatus(), request.getReject(), request.getFormatDateTime(), "Not Checked", "Not Checked" );
                    requests.add(record);
                }else if(!request.getStatus().equals("Pending") && request.getDuty()==null){
                    LeaveRecord record = new LeaveRecord( request.getLeave_id(), request.getLeave_type().getType(), request.getUser().getId(), request.getUser().getFirstName()+" "+request.getUser().getSecondName(), request.getUser().getDepartment().getName(), request.getStartDate(), 
                                                        request.getEnd_date(), request.getStartHalf(), request.getEndHalf(), request.getNumber_of_leave_days(), request.getUser().getSupervisor1(), request.getUser().getSupervisor2(), 
                                                        "No one assigned", request.getSpecial_notes(), request.getStatus(), request.getReject(), request.getFormatDateTime(), request.getCheckBy().getFirstName()+" "+request.getCheckBy().getSecondName(), request.getCheckTime() );
                    requests.add(record);
                }else if(request.getStatus().equals("Pending") && request.getDuty()!=null){
                    LeaveRecord record = new LeaveRecord( request.getLeave_id(), request.getLeave_type().getType(), request.getUser().getId(), request.getUser().getFirstName()+" "+request.getUser().getSecondName(), request.getUser().getDepartment().getName(), request.getStartDate(), 
                                                        request.getEnd_date(), request.getStartHalf(), request.getEndHalf(), request.getNumber_of_leave_days(), request.getUser().getSupervisor1(), request.getUser().getSupervisor2(), 
                                                        request.getDuty().getFirstName()+" "+request.getDuty().getSecondName(), request.getSpecial_notes(), request.getStatus(), request.getReject(), request.getFormatDateTime(), "Not Checked", "Not Checked" );
                    requests.add(record);
                }else{
                    LeaveRecord record = new LeaveRecord( request.getLeave_id(), request.getLeave_type().getType(), request.getUser().getId(), request.getUser().getFirstName()+" "+request.getUser().getSecondName(), request.getUser().getDepartment().getName(), request.getStartDate(), 
                                                    request.getEnd_date(), request.getStartHalf(), request.getEndHalf(), request.getNumber_of_leave_days(), request.getUser().getSupervisor1(), request.getUser().getSupervisor2(), request.getDuty().getFirstName()+" "+request.getDuty().getSecondName(),
                                                    request.getSpecial_notes(), request.getStatus(), request.getReject(), request.getFormatDateTime() , request.getCheckBy().getFirstName()+" "+request.getCheckBy().getSecondName(), request.getCheckTime() );
                    requests.add(record);
                }                                      
            }
            
            LOGGER.info(">>> Successfully get all leave requests. (By user ==> "+userId+")");
            return ResponseEntity.ok(new ApiResponse(true, requests));
        } catch(Exception e) {
            LOGGER.error(">>> Unable to get all leave requests. (By user ==> "+userId+")", e.getMessage());
            return ResponseEntity.ok(new ApiResponse(false, "Unable to get all leave requests"));
        }
    }

    public ResponseEntity<?> getOneEmployeeLeaveRequests(Long employeeId, Long userId) {
        try {
            User employee = userRepository.getOne(employeeId);
            List<LeaveRequest> all = leaveRequestRepository.findByUser(employee);
            List<LeaveRecord> requests = new ArrayList<LeaveRecord>();

            for (LeaveRequest request : all) {
                if(request.getStatus().equals("Pending") && request.getDuty()==null){
                    LeaveRecord record = new LeaveRecord( request.getLeave_id(), request.getLeave_type().getType(), request.getUser().getId(), request.getUser().getFirstName()+" "+request.getUser().getSecondName(), request.getUser().getDepartment().getName(), request.getStartDate(), 
                                                        request.getEnd_date(), request.getStartHalf(), request.getEndHalf(), request.getNumber_of_leave_days(), request.getUser().getSupervisor1(), request.getUser().getSupervisor2(), 
                                                        "No one assigned", request.getSpecial_notes(), request.getStatus(), request.getReject(), request.getFormatDateTime(), "Not Checked", "Not Checked" );
                    requests.add(record);
                }else if(!request.getStatus().equals("Pending") && request.getDuty()==null){
                    LeaveRecord record = new LeaveRecord( request.getLeave_id(), request.getLeave_type().getType(), request.getUser().getId(), request.getUser().getFirstName()+" "+request.getUser().getSecondName(), request.getUser().getDepartment().getName(), request.getStartDate(), 
                                                        request.getEnd_date(), request.getStartHalf(), request.getEndHalf(), request.getNumber_of_leave_days(), request.getUser().getSupervisor1(), request.getUser().getSupervisor2(), 
                                                        "No one assigned", request.getSpecial_notes(), request.getStatus(), request.getReject(), request.getFormatDateTime(), request.getCheckBy().getFirstName()+" "+request.getCheckBy().getSecondName(), request.getCheckTime() );
                    requests.add(record);
                }else if(request.getStatus().equals("Pending") && request.getDuty()!=null){
                    LeaveRecord record = new LeaveRecord( request.getLeave_id(), request.getLeave_type().getType(), request.getUser().getId(), request.getUser().getFirstName()+" "+request.getUser().getSecondName(), request.getUser().getDepartment().getName(), request.getStartDate(), 
                                                        request.getEnd_date(), request.getStartHalf(), request.getEndHalf(), request.getNumber_of_leave_days(), request.getUser().getSupervisor1(), request.getUser().getSupervisor2(), 
                                                        request.getDuty().getFirstName()+" "+request.getDuty().getSecondName(), request.getSpecial_notes(), request.getStatus(), request.getReject(), request.getFormatDateTime(), "Not Checked", "Not Checked" );
                    requests.add(record);
                }else{
                    LeaveRecord record = new LeaveRecord( request.getLeave_id(), request.getLeave_type().getType(), request.getUser().getId(), request.getUser().getFirstName()+" "+request.getUser().getSecondName(), request.getUser().getDepartment().getName(), request.getStartDate(), 
                                                    request.getEnd_date(), request.getStartHalf(), request.getEndHalf(), request.getNumber_of_leave_days(), request.getUser().getSupervisor1(), request.getUser().getSupervisor2(), request.getDuty().getFirstName()+" "+request.getDuty().getSecondName(),
                                                    request.getSpecial_notes(), request.getStatus(), request.getReject(), request.getFormatDateTime() , request.getCheckBy().getFirstName()+" "+request.getCheckBy().getSecondName(), request.getCheckTime() );
                    requests.add(record);
                }                                      
            }

            LOGGER.info(">>> Successfully get leave requests of "+employee.getUserId()+". (By user ==> "+userId+")");
            return ResponseEntity.ok(new ApiResponse(true, requests));
        } catch(Exception e) {
            LOGGER.error(">>> Unable to get leave requests of user "+employeeId+". (By user ==> "+userId+")", e.getMessage());
            return ResponseEntity.ok(new ApiResponse(false, "Unable to get leave requests"));
        }
    }

    public ResponseEntity<?> addRequest(LeaveRequest leaveRequest, Long userId) {
        try {
            LocalDateTime datetime1 = LocalDateTime.now();  
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");  
            String formatDateTime = datetime1.format(format);
            leaveRequest.setFormatDateTime(formatDateTime);
            leaveRequest.setUser(userRepository.getOne(userId));
            leaveRequestRepository.save(leaveRequest);
            LOGGER.info(">>> Successfully add the leave request. (By user ==> "+userId+")");
            return ResponseEntity.ok(new ApiResponse(true, "Successfully submit your leave request"));
        } catch(Exception e) {
            LOGGER.error(">>> Unable to add the leave request. (By user ==> "+userId+")", e.getMessage());
            return ResponseEntity.ok(new ApiResponse(false, "Unable to add the leave request"));
        }
    }

    public ResponseEntity<?> getPending(String pending, Long userId) {
        try {
            List<LeaveRequest> pendingLeaves = leaveRequestRepository.findByStatus(pending);
            List<LeaveRecord> requests = new ArrayList<LeaveRecord>();

            for (LeaveRequest request : pendingLeaves) {
                if(request.getDuty()==null){
                    LeaveRecord record = new LeaveRecord( request.getLeave_id(), request.getLeave_type().getType(), request.getUser().getId(), request.getUser().getFirstName()+" "+request.getUser().getSecondName(), request.getUser().getDepartment().getName(), request.getStartDate(), 
                                                        request.getEnd_date(), request.getStartHalf(), request.getEndHalf(), request.getNumber_of_leave_days(), request.getUser().getSupervisor1(), request.getUser().getSupervisor2(), 
                                                        "No one assigned", request.getSpecial_notes(), request.getStatus(), request.getReject(), request.getFormatDateTime());
                    requests.add(record);
                }else{
                    LeaveRecord record = new LeaveRecord( request.getLeave_id(), request.getLeave_type().getType(), request.getUser().getId(), request.getUser().getFirstName()+" "+request.getUser().getSecondName(), request.getUser().getDepartment().getName(), request.getStartDate(), 
                                                    request.getEnd_date(), request.getStartHalf(), request.getEndHalf(), request.getNumber_of_leave_days(), request.getUser().getSupervisor1(), request.getUser().getSupervisor2(),
                                                    request.getDuty().getFirstName()+" "+request.getDuty().getSecondName(), request.getSpecial_notes(), request.getStatus(), request.getReject(), request.getFormatDateTime());
                    requests.add(record);
                }                                      
            }
            LOGGER.info(">>> Successfully get the pending leave requests. (By user ==> "+userId+")");
            return ResponseEntity.ok(new ApiResponse(true, requests));
        } catch(Exception e) {
            LOGGER.error(">>> Unable to get the pending leave requests. (By user ==> "+userId+")", e.getMessage());
            return ResponseEntity.ok(new ApiResponse(false, "Unable to get the pending leave requests"));
        }
    }

    public ResponseEntity<?> deleteLeave(Integer id, Long userId) {
        try {
            LeaveRequest leaveRequest = leaveRequestRepository.getOne(id);
            User emp = leaveRequest.getUser();

            if(emp.getId().equals(userId)) {
                LOGGER.warn(">>> Not allowed to delete the own leave requests. (By user ==> "+userId+")");
                return ResponseEntity.ok(new ApiResponse(false, "Not allowed to delete the own leave requests"));
            }

            User supervisor1 = userRepository.findByFirstName(emp.getSupervisor1());
            User supervisor2 = userRepository.findByFirstName(emp.getSupervisor2());
            
            if(leaveRequest.getStatus().equals("Approved")){
                LeaveType type = leaveRequest.getLeave_type();
                Float days = leaveRequest.getNumber_of_leave_days();

                LeaveCount filter = leaveCountRepository.findByUserAndType(emp, type);
                filter.setCount(filter.getCount()-days);
                leaveCountRepository.save(filter);
                
                leaveRequestRepository.deleteById(id);   
                mailDelete(leaveRequest);
                mailSupDelete(supervisor1, leaveRequest);
                mailSupDelete(supervisor2, leaveRequest);
                LOGGER.error(">>> Unable to delete the pending leave requests. (By user ==> "+userId+")");
                return ResponseEntity.ok(new ApiResponse(true, "Remove the approved request"));
            }else if(leaveRequest.getStatus().equals("Rejected")){
                leaveRequestRepository.deleteById(id);
                mailDelete(leaveRequest);
                mailSupDelete(supervisor1, leaveRequest);
                mailSupDelete(supervisor2, leaveRequest);
                LOGGER.info(">>> Unable to delete the pending leave requests. (By user ==> "+userId+")");
                return ResponseEntity.ok(new ApiResponse(true, "Remove the rejected request"));
                
            }else{                
                LOGGER.warn(">>> Unable to delete the pending leave requests. (By user ==> "+userId+")");
                return ResponseEntity.ok(new ApiResponse(false, "Can't remove the pending leave requests"));
            }
        } catch(Exception e) {
            LOGGER.error(">>> Unable to delete the leave requests. (By user ==> "+userId+")", e.getMessage());
            return ResponseEntity.ok(new ApiResponse(false, "Unable to delete the leave requests"));
        }
    }

    private void mailDelete (LeaveRequest leave ) {
        User employee = leave.getUser();
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(employee.getEmail());
		msg.setSubject("Remove the leave request.");
		msg.setText("Hi "+employee.getFirstName()+" "+employee.getSecondName()+",\n\n"+
					"Deleted your following "+leave.getStatus()+" leave request. \n\n"+
					"Date of request: "+leave.getFormatDateTime()+"\n"+
                    "Leave type: "+leave.getLeave_type().getType()+"\n"+
                    "Number of leave days: "+leave.getNumber_of_leave_days()+"\n"+
                    "Leave period: "+leave.getStartDate()+ " to " +leave.getEnd_date()+"\n\n"+
					"Thanks. \nBest Regards"
                    );
                     
		try {
            javaMailSender.send(msg);
			LOGGER.info(">>> E-mail send to ==> "+employee.getEmail());
		}catch (Exception e){
			LOGGER.error(">>> (MailSender) ==> "+e);
		}
    }

    private void mailSupDelete (User supervisor, LeaveRequest leave ) {
        User employee = leave.getUser();

        SimpleMailMessage mail= new SimpleMailMessage();
        mail.setTo(supervisor.getEmail());
		mail.setSubject("Remove the leave request of "+employee.getFirstName());
		mail.setText("Hi "+supervisor.getFirstName() +" "+ supervisor.getSecondName()+",\n\n"+
					"Deleted the following "+leave.getStatus()+" leave request of "+employee.getFirstName()+" "+employee.getSecondName()+". \n\n"+
					"Date of request: "+leave.getFormatDateTime()+"\n"+
                    "Leave type: "+leave.getLeave_type().getType()+"\n"+
                    "Number of leave days: "+leave.getNumber_of_leave_days()+"\n"+
                     "Leave period: "+leave.getStartDate()+ " to " +leave.getEnd_date()+"\n\n"+
					"Thanks. \nBest Regards"
                    );
        try {
            javaMailSender.send(mail);
			LOGGER.info(">>> E-mail send to ==> "+employee.getEmail());
		}catch (Exception e){
			LOGGER.error(">>> (MailSender) ==> "+e);
		}
    }

    public ResponseEntity<?> approveRequest(Long check_id, LeaveRequest leaveRequest) {
        try {             
            Integer id = leaveRequest.getLeave_id();
            LeaveRequest request = leaveRequestRepository.getOne(id);

            User emp = request.getUser();

            if(emp.getId().equals(check_id)) {
                LOGGER.warn(">>> Not allowed to approve the own leave requests. (By user ==> "+check_id+")");
                return ResponseEntity.ok(new ApiResponse(false, "Not allowed to approve the own leave requests"));
            }

            request.setStatus("Approved");

            User checked_by = userRepository.getOne(check_id);
            request.setCheckBy(checked_by);

            LocalDateTime datetime1 = LocalDateTime.now(); 
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");  
            String checkTime = datetime1.format(format);
            request.setCheckTime(checkTime);

            leaveRequestRepository.save(request);

            LeaveType type = request.getLeave_type();
            Float days = request.getNumber_of_leave_days();

            if(leaveCountRepository.existsByUserAndType(emp, type)) { 
                LeaveCount filter = leaveCountRepository.findByUserAndType(emp, type);
                filter.setCount(filter.getCount()+days);
                leaveCountRepository.save(filter);
            }else{
                LeaveCount count = new LeaveCount(emp, type, days);
                leaveCountRepository.save(count);
            }
            acceptRequest(request);

            if(request.getDuty()!=null){
                mailDutyCover(request.getDuty() , request);
            }
            
            LOGGER.info(">>> Successfully approve the leave request. (By user ==> "+check_id+")");
            return ResponseEntity.ok(new ApiResponse(true, "Approved the leave requests"));
        } catch(Exception e) {
            LOGGER.error(">>> Unable to approve the leave request. (By user ==> "+check_id+")");
            return ResponseEntity.ok(new ApiResponse(false, "Unable to approve the leave request"));
        }
    }

    private void mailDutyCover (User duty, LeaveRequest leave ) {

        User employee = leave.getUser();
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(duty.getEmail());
		msg.setSubject("Aditional duty to cover.");
		msg.setText("Hi "+duty.getFirstName()+" "+duty.getSecondName()+",\n"+
					"You are asigned to cover the duty of following employee in these day/s. \n\n"+
					"Name: "+employee.getFirstName()+" "+employee.getSecondName()+"\n"+
					"Role: "+employee.getRoles().stream().findFirst().get().getName()+"\n"+
                    "Department: "+employee.getDepartment().getName()+"\n"+
                    "Date/s: "+leave.getStartDate()+" to "+leave.getEnd_date()+"\n\n"+
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

        User employee = leave.getUser();
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(employee.getEmail());
		msg.setSubject("Accept leave request.");
		msg.setText("Hi "+employee.getFirstName()+" "+employee.getSecondName()+",\n\n"+
                    "Accept your following leave request. \n\n"+
                    "Date of request: "+leave.getFormatDateTime()+"\n"+
                    "Leave type: "+leave.getLeave_type().getType()+"\n"+
                    "Number of leave days: "+leave.getNumber_of_leave_days()+"\n"+
                    "Leave period: "+leave.getStartDate()+ " to " +leave.getEnd_date()+"\n\n"+
					"Thanks. \nBest Regards"
					);

		try {
			javaMailSender.send(msg);
			LOGGER.info(">>> E-mail send to ==> "+employee.getEmail());
		}catch (Exception e){
			LOGGER.error(">>> (MailSender) ==> "+e);
		}
    }

    private void mailReject (LeaveRequest leave ) {

        User employee = leave.getUser();
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(employee.getEmail());
		msg.setSubject("Reject Leave Request.");
		msg.setText("Hi "+employee.getFirstName()+" "+employee.getSecondName()+",\n\n"+
                    "Reject your following leave request according to "+ "\""+leave.getReject()+"\""+"\n\n"+
                    "Date of request: "+leave.getFormatDateTime()+"\n"+
                    "Leave type: "+leave.getLeave_type().getType()+"\n"+
                    "Number of leave days: "+leave.getNumber_of_leave_days()+"\n"+
                    "Leave period: "+leave.getStartDate()+ " to " +leave.getEnd_date()+"\n\n"+
					"Sorry for the rejection.\nThanks\nBest Regards"
					);

		try {
			javaMailSender.send(msg);
			LOGGER.info(">>> E-mail send to ==> "+employee.getEmail());
		}catch (Exception e){
			LOGGER.error(">>> (MailSender) ==> "+e);
		}
    }
    
    public ResponseEntity<?> rejectRequest(Integer id, LeaveRequest reason, Long check_id) {
        try {
            LeaveRequest request = leaveRequestRepository.getOne(id);

            User emp = request.getUser();

            if(emp.getId().equals(check_id)) {
                LOGGER.warn(">>> Not allowed to reject the own leave requests. (By user ==> "+check_id+")");
                return ResponseEntity.ok(new ApiResponse(false, "Not allowed to reject the own leave requests"));
            }

            request.setReject(reason.getReject().trim());
            request.setStatus("Rejected");

            User checked_by = userRepository.getOne(check_id);
            request.setCheckBy(checked_by);

            LocalDateTime datetime1 = LocalDateTime.now(); 
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");  
            String checkTime = datetime1.format(format);
            request.setCheckTime(checkTime);

            leaveRequestRepository.save(request);

            mailReject(request);

            LOGGER.info(">>> Successfully reject the leave request. (By user ==> "+check_id+")");
            return ResponseEntity.ok(new ApiResponse(true, "Reject the leave request"));
        } catch(Exception e) {
            LOGGER.error(">>> Unable to reject the leave request. (By user ==> "+check_id+")");
            return ResponseEntity.ok(new ApiResponse(false, "Unable to reject the leave request"));
        }
    }

    public ResponseEntity<?> getAbsence( Long userId) {

        try {
            List<AbsenceUser> absence = new ArrayList<>();
            List<LeaveRequest> requests = leaveRequestRepository.findByStatus("Approved");
            LocalDate today = LocalDate.now(); 

            for (LeaveRequest var : requests) {
                LocalDate start = LocalDate.parse(var.getStartDate());
                LocalDate end = LocalDate.parse(var.getEnd_date());
                List<LocalDate> totalDates = new ArrayList<>();
                while (!start.isAfter(end)) {
                    totalDates.add(start);
                    start = start.plusDays(1);
                }
                if(totalDates.contains(today)){
                    AbsenceUser user = new AbsenceUser(var.getUser().getFirstName()+" "+var.getUser().getSecondName(), var.getUser().getImage());
                    absence.add(user);
                }
            }
            LOGGER.info(">>> Successfully get absence employees. (By user ==> "+userId+")");
            return ResponseEntity.ok(new ApiResponse(true, absence));
        } catch(Exception e) {
            LOGGER.error(">>> Unable to get absence employees. (By user ==> "+userId+")");
            return ResponseEntity.ok(new ApiResponse(false, "Unable to get absence employees"));
        }   
    }

    public ResponseEntity<?> getRequested( Long userId) {

        try {
            List<AbsenceUser> absence = new ArrayList<>();
            List<LeaveRequest> requests = leaveRequestRepository.findByStatus("Pending");
            LocalDate today = LocalDate.now(); 

            for (LeaveRequest var : requests) {
                LocalDate start = LocalDate.parse(var.getStartDate());
                LocalDate end = LocalDate.parse(var.getEnd_date());
                List<LocalDate> totalDates = new ArrayList<>();
                while (!start.isAfter(end)) {
                    totalDates.add(start);
                    start = start.plusDays(1);
                }
                if(totalDates.contains(today)){
                    AbsenceUser user = new AbsenceUser(var.getUser().getFirstName()+" "+var.getUser().getSecondName(), var.getUser().getImage());
                    absence.add(user);
                }
            }
            LOGGER.info(">>> Successfully get leave requested employees. (By user ==> "+userId+")");
            return ResponseEntity.ok(new ApiResponse(true, absence));
        } catch(Exception e) {
            LOGGER.error(">>> Unable to get leave requested employees. (By user ==> "+userId+")");
            return ResponseEntity.ok(new ApiResponse(false, "Unable to get leave requested employees"));
        }    
    }

    public ResponseEntity<?> getRejected( Long userId) {

        try {
            List<AbsenceUser> absence = new ArrayList<>();
            List<LeaveRequest> requests = leaveRequestRepository.findByStatus("Rejected");
            LocalDate today = LocalDate.now(); 

            for (LeaveRequest var : requests) {
                LocalDate start = LocalDate.parse(var.getStartDate());
                LocalDate end = LocalDate.parse(var.getEnd_date());
                List<LocalDate> totalDates = new ArrayList<>();
                while (!start.isAfter(end)) {
                    totalDates.add(start);
                    start = start.plusDays(1);
                }
                if(totalDates.contains(today)){
                    AbsenceUser user = new AbsenceUser(var.getUser().getFirstName()+" "+var.getUser().getSecondName(), var.getUser().getImage());
                    absence.add(user);
                }
            }
            LOGGER.info(">>> Successfully get leave rejected employees. (By user ==> "+userId+")");
            return ResponseEntity.ok(new ApiResponse(true, absence));
        } catch(Exception e) {
            LOGGER.error(">>> Unable to get leave rejected employees. (By user ==> "+userId+")");
            return ResponseEntity.ok(new ApiResponse(false, "Unable to get leave rejected employees"));
        }
    }

    public ResponseEntity<?> getFilterd( Long userId, LeaveFilter filters) {

        List<LeaveRecord> records = new ArrayList<>();
        List<LeaveRequest> leaves = new ArrayList<>(); 

        try {

            if(filters.getSelectWhose()==0){
                if(filters.getSelectDate()==0){
                    leaves = leaveRequestRepository.findAll();
                }
                else if(filters.getSelectDate()==1){
                    leaves = leaveRequestRepository.findByStartDate(filters.getSelectedDate());
                }
                else if(filters.getSelectDate()==2){
                    LocalDate start = LocalDate.parse(filters.getSelectedDate());
                    LocalDate end = LocalDate.parse(filters.getSelectedOtherDate());
                    List<String> totalDates = new ArrayList<>();

                    while (!start.isAfter(end)) {
                        totalDates.add(start.toString());
                        start = start.plusDays(1);
                    }

                    leaves = leaveRequestRepository.findByStartDateIn(totalDates);
                }
                else if(filters.getSelectDate()==3){
                    LocalDate start = LocalDate.parse(filters.getSelectedDate());
                    leaves = leaveRequestRepository.findAll();

                    leaves = leaves.stream().filter(x -> LocalDate.parse(x.getStartDate()).isAfter(start)).collect(Collectors.toList());
                }
                else if(filters.getSelectDate()==4){
                    LocalDate start = LocalDate.parse(filters.getSelectedDate());
                    leaves = leaveRequestRepository.findAll();
                    
                    leaves = leaves.stream().filter(x -> LocalDate.parse(x.getStartDate()).isBefore(start)).collect(Collectors.toList());
                }
            }
            else if(filters.getSelectWhose()==1){
                User user = userRepository.getOne(userId);
                if(filters.getSelectDate()==0){
                    leaves = leaveRequestRepository.findByUser(user);
                }
                else if(filters.getSelectDate()==1){
                    leaves = leaveRequestRepository.findByUserAndStartDate(user, filters.getSelectedDate());
                }
                else if(filters.getSelectDate()==2){
                    LocalDate start = LocalDate.parse(filters.getSelectedDate());
                    LocalDate end = LocalDate.parse(filters.getSelectedOtherDate());
                    List<String> totalDates = new ArrayList<>();

                    while (!start.isAfter(end)) {
                        totalDates.add(start.toString());
                        start = start.plusDays(1);
                    }

                    leaves = leaveRequestRepository.findByUserAndStartDateIn(user, totalDates);
                }
                else if(filters.getSelectDate()==3){
                    LocalDate start = LocalDate.parse(filters.getSelectedDate());
                    leaves = leaveRequestRepository.findByUser(user);

                    leaves = leaves.stream().filter(x -> LocalDate.parse(x.getStartDate()).isAfter(start)).collect(Collectors.toList());
                }
                else if(filters.getSelectDate()==4){
                    LocalDate start = LocalDate.parse(filters.getSelectedDate());
                    leaves = leaveRequestRepository.findByUser(user);
                    
                    leaves = leaves.stream().filter(x -> LocalDate.parse(x.getStartDate()).isBefore(start)).collect(Collectors.toList());
                }
            }
            else if(filters.getSelectWhose()==2){
                Department department = departmentRepository.getOne(filters.getSelectedWhose());
                List<User> users = userRepository.findByDepartment(department);
                if(filters.getSelectDate()==0){
                    leaves = leaveRequestRepository.findByUserIn(users);
                }
                else if(filters.getSelectDate()==1){
                    leaves = leaveRequestRepository.findByUserInAndStartDate(users, filters.getSelectedDate());
                }
                else if(filters.getSelectDate()==2){
                    LocalDate start = LocalDate.parse(filters.getSelectedDate());
                    LocalDate end = LocalDate.parse(filters.getSelectedOtherDate());
                    List<String> totalDates = new ArrayList<>();

                    while (!start.isAfter(end)) {
                        totalDates.add(start.toString());
                        start = start.plusDays(1);
                    }

                    leaves = leaveRequestRepository.findByUserInAndStartDateIn(users, totalDates);
                }
                else if(filters.getSelectDate()==3){
                    LocalDate start = LocalDate.parse(filters.getSelectedDate());
                    leaves = leaveRequestRepository.findByUserIn(users);

                    leaves = leaves.stream().filter(x -> LocalDate.parse(x.getStartDate()).isAfter(start)).collect(Collectors.toList());
                }
                else if(filters.getSelectDate()==4){
                    LocalDate start = LocalDate.parse(filters.getSelectedDate());
                    leaves = leaveRequestRepository.findByUserIn(users);
                    
                    leaves = leaves.stream().filter(x -> LocalDate.parse(x.getStartDate()).isBefore(start)).collect(Collectors.toList());
                }
            }
            else if(filters.getSelectWhose()==3){
                User user = userRepository.getOne(filters.getSelectedWhose());
                if(filters.getSelectDate()==0){
                    leaves = leaveRequestRepository.findByUser(user);
                }
                else if(filters.getSelectDate()==1){
                    leaves = leaveRequestRepository.findByUserAndStartDate(user, filters.getSelectedDate());
                }
                else if(filters.getSelectDate()==2){
                    LocalDate start = LocalDate.parse(filters.getSelectedDate());
                    LocalDate end = LocalDate.parse(filters.getSelectedOtherDate());
                    List<String> totalDates = new ArrayList<>();

                    while (!start.isAfter(end)) {
                        totalDates.add(start.toString());
                        start = start.plusDays(1);
                    }

                    leaves = leaveRequestRepository.findByUserAndStartDateIn(user, totalDates);
                }
                else if(filters.getSelectDate()==3){
                    LocalDate start = LocalDate.parse(filters.getSelectedDate());
                    leaves = leaveRequestRepository.findByUser(user);

                    leaves = leaves.stream().filter(x -> LocalDate.parse(x.getStartDate()).isAfter(start)).collect(Collectors.toList());
                }
                else if(filters.getSelectDate()==4){
                    LocalDate start = LocalDate.parse(filters.getSelectedDate());
                    leaves = leaveRequestRepository.findByUser(user);
                    
                    leaves = leaves.stream().filter(x -> LocalDate.parse(x.getStartDate()).isBefore(start)).collect(Collectors.toList());
                }
            }

            for (LeaveRequest request : leaves) {
                if(request.getStatus().equals("Pending") && request.getDuty()==null){
                    LeaveRecord record = new LeaveRecord( request.getLeave_id(), request.getLeave_type().getType(), request.getUser().getId(), request.getUser().getFirstName()+" "+request.getUser().getSecondName(), request.getUser().getDepartment().getName(), request.getStartDate(), 
                                                        request.getEnd_date(), request.getStartHalf(), request.getEndHalf(), request.getNumber_of_leave_days(), request.getUser().getSupervisor1(), request.getUser().getSupervisor2(), 
                                                        "No one assigned", request.getSpecial_notes(), request.getStatus(), request.getReject(), request.getFormatDateTime(), "Not Checked", "Not Checked" );
                    records.add(record);
                }else if(!request.getStatus().equals("Pending") && request.getDuty()==null){
                    LeaveRecord record = new LeaveRecord( request.getLeave_id(), request.getLeave_type().getType(), request.getUser().getId(), request.getUser().getFirstName()+" "+request.getUser().getSecondName(), request.getUser().getDepartment().getName(), request.getStartDate(), 
                                                        request.getEnd_date(), request.getStartHalf(), request.getEndHalf(), request.getNumber_of_leave_days(), request.getUser().getSupervisor1(), request.getUser().getSupervisor2(), 
                                                        "No one assigned", request.getSpecial_notes(), request.getStatus(), request.getReject(), request.getFormatDateTime(), request.getCheckBy().getFirstName()+" "+request.getCheckBy().getSecondName(), request.getCheckTime() );
                    records.add(record);
                }else if(request.getStatus().equals("Pending") && request.getDuty()!=null){
                    LeaveRecord record = new LeaveRecord( request.getLeave_id(), request.getLeave_type().getType(), request.getUser().getId(), request.getUser().getFirstName()+" "+request.getUser().getSecondName(), request.getUser().getDepartment().getName(), request.getStartDate(), 
                                                        request.getEnd_date(), request.getStartHalf(), request.getEndHalf(), request.getNumber_of_leave_days(), request.getUser().getSupervisor1(), request.getUser().getSupervisor2(), 
                                                        request.getDuty().getFirstName()+" "+request.getDuty().getSecondName(), request.getSpecial_notes(), request.getStatus(), request.getReject(), request.getFormatDateTime(), "Not Checked", "Not Checked" );
                    records.add(record);
                }else{
                    LeaveRecord record = new LeaveRecord( request.getLeave_id(), request.getLeave_type().getType(), request.getUser().getId(), request.getUser().getFirstName()+" "+request.getUser().getSecondName(), request.getUser().getDepartment().getName(), request.getStartDate(), 
                                                    request.getEnd_date(), request.getStartHalf(), request.getEndHalf(), request.getNumber_of_leave_days(), request.getUser().getSupervisor1(), request.getUser().getSupervisor2(), request.getDuty().getFirstName()+" "+request.getDuty().getSecondName(),
                                                    request.getSpecial_notes(), request.getStatus(), request.getReject(), request.getFormatDateTime() , request.getCheckBy().getFirstName()+" "+request.getCheckBy().getSecondName(), request.getCheckTime() );
                    records.add(record);
                }                                      
            }
            
            LOGGER.info(">>> Successfully get filtered leave records. (By user ==> "+userId+")");
            return ResponseEntity.ok(new ApiResponse(true, records));
        } catch(Exception e) {
            LOGGER.error(">>> Unable to get filtered leave records. (By user ==> "+userId+")");
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(false, "Unable to get filtered leave records"));
        }
            
    }
}