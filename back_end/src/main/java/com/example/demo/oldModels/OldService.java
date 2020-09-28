// package com.example.demo.oldModels;

// import com.example.demo.exception.AppException;
// import com.example.demo.model.*;
// import com.example.demo.oldModels.payloads.UserPayload;
// import com.example.demo.payload.ApiResponse;
// import com.example.demo.repository.*;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
// import java.net.URI;
// import java.text.SimpleDateFormat;
// import java.time.LocalDate;
// import java.time.LocalDateTime;
// import java.time.format.DateTimeFormatter;
// import java.util.Collections;
// import java.util.Date;
// import java.util.List;
// import java.util.Locale;
// import java.util.stream.Collectors;

// @org.springframework.stereotype.Service
// public class OldService {

//     @Autowired
//     private OldJDBC userJDBC;

//     @Autowired
//     PasswordEncoder passwordEncoder;

//     @Autowired
//     UserRepository userRepository;

//     @Autowired
//     RoleRepository roleRepository;

//     @Autowired
//     private ContactRepository contactRepository;

//     @Autowired
//     private CalenderRepository calenderRepository;

//     @Autowired
//     private DepartmentRepository departmentRepository;

//     @Autowired
//     private LeaveTypeRepository leaveTypeRepository;

//     @Autowired
//     private LeaveRequestRepository leaveRequestRepository;

//     @Autowired
//     private LeaveCountRepository leaveCountRepository;

//     @Value("${admin.id}")
//     private Long adminId;

//     private static final Logger LOGGER = LoggerFactory.getLogger(com.example.demo.service.CalenderService.class);

//     public ResponseEntity<?> saveDepartments() {
//         try {
//             List<Department> departments = userJDBC.getDepartments();

//             for (Department department : departments) {
//                 System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + department.getName());
//                 departmentRepository.save(department);
//             }
//             LOGGER.info(">>> Successfully entered all departments");
//             return ResponseEntity.ok(new ApiResponse(true, "Successfully entered all departments"));
//         }catch(Exception e){
//             LOGGER.error(">>> Unable to enter the departments.", e.getMessage());
//             e.printStackTrace();
//             return ResponseEntity.ok(new ApiResponse(false, "Unable to enter the departments"));
//         }
//     }

//     public ResponseEntity<?> saveLeaveTypes() {
//         try {
//             List<LeaveType> types = userJDBC.getLeaveTypes();

//             for (LeaveType type : types) {
//                 System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + type.getType());
//                 leaveTypeRepository.save(type);
//             }
//             LOGGER.info(">>> Successfully entered all leave types");
//             return ResponseEntity.ok(new ApiResponse(true, "Successfully entered all leave types"));
//         }catch(Exception e){
//             LOGGER.error(">>> Unable to enter the leave types.", e.getMessage());
//             e.printStackTrace();
//             return ResponseEntity.ok(new ApiResponse(false, "Unable to enter the leave types"));
//         }
//     }

//     public ResponseEntity<?> saveEmployees() {
//         try {
//             List<UserPayload> employees = userJDBC.getEmployees();

//             String password = "VizuaMatix@123";
//             String encodedPassword = passwordEncoder.encode(password);

//             List<User> users = employees.stream().map( x -> new User(x.getId(), x.getUserId(), x.getUserId(), x.getFirstName(),
//                     x.getLastName(), x.getInitials(), "Male",
//                     x.getEmail().trim(), "No residential address", "0712345678", departmentRepository.getOne(x.getDepartmentId()), null, null, null, x.getJoinDate(),
//                     x.getConfirmDate(), x.getResignedDate(), encodedPassword, x.getStatus(), 14.0F,
//                     7.0F, 7.0F, null, "No permanent address", null, "None", "Buddhist", null) ).collect(Collectors.toList());

//             for (User user : users) {
//                 Role userRole = roleRepository.findById(6L).orElseThrow(() -> new AppException("User Role not set."));
//                 user.setRoles(Collections.singleton(userRole));
//                 userRepository.save(user);
//                 System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + user.getUserId());
// //                URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/{username}").buildAndExpand(user.getUserId()).toUri();
// //                ResponseEntity.created(location).body(new ApiResponse(true, "Successfully create the employee."));
//             }
//             LOGGER.info(">>> Successfully entered all employees");
//             return ResponseEntity.ok(new ApiResponse(true, "Successfully entered all employees"));
//         }catch(Exception e){
//             LOGGER.error(">>> Unable to enter the employees.", e.getMessage());
//             e.printStackTrace();
//             return ResponseEntity.ok(new ApiResponse(false, "Unable to enter the employees"));
//         }
//     }

//     public ResponseEntity<?> saveAdmin() {
//         try {
//             String password = "VizuaMatix@123";
//             String encodedPassword = passwordEncoder.encode(password);

//             User user = new User(0L,"0000", "0000", "System",
//                     "Admin", "SA", "Male",
//                     "", "No residential address", "0712345678", departmentRepository.getOne(5L), null, null, null, null,
//                     null, "Not resign", encodedPassword, "Working", 14.0F,
//                     7.0F, 7.0F, null, "No permanent address", null, "None", "Buddhist", null) ;

//             Role userRole = roleRepository.findById(6L).orElseThrow(() -> new AppException("User Role not set."));
//             user.setRoles(Collections.singleton(userRole));
//             userRepository.save(user);
//             System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + user.getUserId());

//             LOGGER.info(">>> Successfully entered admin");
//             return ResponseEntity.ok(new ApiResponse(true, "Successfully entered admin"));
//         }catch(Exception e){
//             LOGGER.error(">>> Unable to enter the admin.", e.getMessage());
//             e.printStackTrace();
//             return ResponseEntity.ok(new ApiResponse(false, "Unable to enter the admin"));
//         }
//     }

//     public ResponseEntity<?> saveContacts() {
//         try {
//             List<Contact> contacts = userJDBC.getContacts();

//             for (Contact contact : contacts) {
//                 System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + contact.getUser().getUserId());
//                 contactRepository.save(contact);
//             }
//             LOGGER.info(">>> Successfully entered all contact data");
//             return ResponseEntity.ok(new ApiResponse(true, "Successfully entered all contact data"));
//         }catch(Exception e){
//             LOGGER.error(">>> Unable to enter the contact data.", e.getMessage());
//             e.printStackTrace();
//             return ResponseEntity.ok(new ApiResponse(false, "Unable to enter the contact data"));
//         }
//     }

//     public ResponseEntity<?> saveCompanyLeaves() {
//         try {
//             List<Calender> contacts = userJDBC.getCompanyLeaves();

//             LocalDateTime datetime1 = LocalDateTime.now();
//             DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//             String createdAt = datetime1.format(format);

//             for (Calender event : contacts) {
//                 System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + event.getDate());
//                 event.setCreatedAt(createdAt);
//                 event.setCreatedBy(userRepository.getOne(adminId));
//                 calenderRepository.save(event);
//             }
//             LOGGER.info(">>> Successfully entered all company events");
//             return ResponseEntity.ok(new ApiResponse(true, "Successfully entered all company events"));
//         }catch(Exception e){
//             LOGGER.error(">>> Unable to enter the company events.", e.getMessage());
//             e.printStackTrace();
//             return ResponseEntity.ok(new ApiResponse(false, "Unable to enter the company events"));
//         }
//     }

//     public ResponseEntity<?> saveLeaveDetails() {
//         try {
//             List<LeaveRequest> leaveRequests = userJDBC.getLeaveDetails();

//             for (LeaveRequest leaveRequest : leaveRequests) {

// //                if(!leaveRequestRepository.existsByUserAndFormatDateTime(leaveRequest.getUser(), leaveRequest.getFormatDateTime())) {
//                     System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + leaveRequest.getLeave_id());

//                     SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                     SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
//                     Date startDate = inputFormat.parse(leaveRequest.getStartDate());
//                     Date endDate = inputFormat.parse(leaveRequest.getEnd_date());
//                     String formattedStartDate = outputFormat.format(startDate);
//                     String formattedEndDate = outputFormat.format(endDate);

//                     leaveRequest.setStartDate(formattedStartDate);
//                     leaveRequest.setEnd_date(formattedEndDate);

//                     leaveRequestRepository.save(leaveRequest);

//                     User emp = leaveRequest.getUser();
//                     LeaveType type = leaveRequest.getLeave_type();
//                     Float days = leaveRequest.getNumber_of_leave_days();
//                     String status = leaveRequest.getStatus();

//                     LocalDate startDateForFilter = LocalDate.parse(formattedStartDate);

//                     if(status.equals("Pending")) {
//                         if(leaveCountRepository.existsByUserAndTypeAndYear(emp, type, startDateForFilter.getYear())) {
//                             LeaveCount filter = leaveCountRepository.findByUserAndTypeAndYear(emp, type, startDateForFilter.getYear());
//                             filter.setPending(filter.getPending()+days);
//                             leaveCountRepository.save(filter);
//                         }else{
//                             LeaveCount count = new LeaveCount(emp, type, 0.0F, days, startDateForFilter.getYear());
//                             leaveCountRepository.save(count);
//                         }
//                     } else if(status.equals("Approved")) {
//                         if(leaveCountRepository.existsByUserAndTypeAndYear(emp, type, startDateForFilter.getYear())) {
//                             LeaveCount filter = leaveCountRepository.findByUserAndTypeAndYear(emp, type, startDateForFilter.getYear());
//                             filter.setCount(filter.getCount()+days);
//                             leaveCountRepository.save(filter);
//                         }else{
//                             LeaveCount count = new LeaveCount(emp, type, days, 0.0F, startDateForFilter.getYear());
//                             leaveCountRepository.save(count);
//                         }
//                     }
// //                }
//             }
//             LOGGER.info(">>> Successfully entered all leave requests");
//             return ResponseEntity.ok(new ApiResponse(true, "Successfully entered all leave requests"));
//         }catch(Exception e){
//             LOGGER.error(">>> Unable to enter the leave requests.", e.getMessage());
//             e.printStackTrace();
//             return ResponseEntity.ok(new ApiResponse(false, "Unable to enter the leave requests"));
//         }
//     }
// }