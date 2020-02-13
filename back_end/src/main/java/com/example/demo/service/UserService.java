package com.example.demo.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.example.demo.model.User;
import com.example.demo.payload.ApiResponse;
import com.example.demo.payload.Employee;
import com.example.demo.payload.Supervisor;
import com.example.demo.payload.UpdateUser;
import com.example.demo.model.Department;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.DepartmentRepository;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public ResponseEntity<?> getAll( Long userId) {
        try {
            List<User> users = userRepository.findAll();
            List<Employee> employees = new ArrayList<Employee>();

            for (User user : users) {
                Employee employee = new Employee(user.getId(),user.getUserId(),user.getFirstName(),user.getSecondName(),user.getInitials(),user.getGender(),user.getEmail(),
                user.getResidence(),user.getContact(),user.getRoles().stream().findFirst().get().getName().toString(),user.getDepartment().getName(), user.getDesignation(),
                user.getSupervisor1(),user.getSupervisor2(),user.getJoinDate(),user.getConfirmDate(), user.getResignDate(), user.getStatus(),user.getAnnual(),
                user.getCasual(),user.getMedical());
                employees.add(employee);
            }

            LOGGER.info(">>> Successfully get all employees. (By user ==> "+userId+")");
            return ResponseEntity.ok(new ApiResponse(true, employees));
        } catch(Exception e) {
            LOGGER.error(">>> Unable to get all employees. (By user ==> "+userId+")", e.getMessage());
            return ResponseEntity.ok(new ApiResponse(false, "Unable to get all employees"));
        } 
    }

    public ResponseEntity<?> resignEmployee(Employee emp, Long userId) {
        try {
            User employee = userRepository.getOne(emp.getId());
            employee.setStatus("Resign");
            employee.setResignDate(emp.getResignDate());
            userRepository.save(employee);
            LOGGER.info(">>> Successfully resign the employee "+employee.getFirstName()+". (By user ==> "+userId+")");
            return ResponseEntity.ok(new ApiResponse(true, "Successfully resign the employee"));
        } catch(Exception e) {
            LOGGER.error(">>> Unable to resign the employee. (By user ==> "+userId+")", e.getMessage());
            return ResponseEntity.ok(new ApiResponse(false, "Unable to resign the employee"));
        } 
    }

    @Value("${spring.datasource.url}")
    private static String urlToDb;

    @Value("${spring.datasource.username}")
    private static String username;

    @Value("${spring.datasource.password}")
    private static String password;

    static void sql(Long emp_id, Long role_id) {
        try (Connection conn = DriverManager.getConnection(urlToDb, username, password)) {
    
            String sql = "UPDATE user_roles SET role_id=? WHERE user_id=?";

            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, role_id.toString());
            statement.setString(2, emp_id.toString());
            
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                LOGGER.info(">>> An existing user was updated successfully.");
            } 
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            LOGGER.error(">>> Unable to update the employee. SQL connection error", ex.getMessage());
        }
    }

    public ResponseEntity<?> updateEmployee(Long id, UpdateUser employee, Long userId) {

        try {
            User employeeToUpdate = userRepository.getOne(id);
            employeeToUpdate.setUserId(employee.getUserId());
            employeeToUpdate.setUsername(employee.getEmail());
            employeeToUpdate.setFirstName(employee.getFirstName());
            employeeToUpdate.setSecondName(employee.getSecondName());
            employeeToUpdate.setInitials(employee.getInitials());
            employeeToUpdate.setGender(employee.getGender());
            employeeToUpdate.setEmail(employee.getEmail());
            employeeToUpdate.setResidence(employee.getResidence());
            employeeToUpdate.setContact(employee.getContact());

            String role_name = employee.getRole();

            if(role_name.equals("Supervisor")){
                sql(id, 1L);      
            }else if(role_name.equals("Permanent")){
                sql(id, 2L);  
            }else if(role_name.equals("Probation")){
                sql(id, 3L);  
            }else if(role_name.equals("Intern")){
                sql(id, 4L);  
            }else if(role_name.equals("Contract")){
                sql(id, 5L);  
            }else if(role_name.equals("Admin")){
                sql(id, 6L);  
            } 

            Department department = departmentRepository.findByName(employee.getDepartment());
            employeeToUpdate.setDepartment(department);
            employeeToUpdate.setDesignation(employee.getDesignation());
            employeeToUpdate.setSupervisor1(employee.getSupervisor1());
            employeeToUpdate.setSupervisor2(employee.getSupervisor2());
            employeeToUpdate.setJoinDate(employee.getJoinDate());
            employeeToUpdate.setConfirmDate(employee.getConfirmDate());
            employeeToUpdate.setAnnual(employee.getAnnual());
            employeeToUpdate.setCasual(employee.getCasual());
            employeeToUpdate.setMedical(employee.getMedical());

            userRepository.save(employeeToUpdate);

            LOGGER.info(">>> Successfully update the employee "+employee.getFirstName()+". (By user ==> "+userId+")");
            return ResponseEntity.ok(new ApiResponse(true, "Successfully update the employee"));
        } catch(Exception e) {
            LOGGER.error(">>> Unable to resign the employee. (By user ==> "+userId+")", e.getMessage());
            return ResponseEntity.ok(new ApiResponse(false, "Unable to update the employee"));
        } 
    }

    public ResponseEntity<?> searchById(Long id, Long userId) {

        try { 
            User user = userRepository.getOne(id);

            Employee employee = new Employee(user.getId(),user.getUserId(),user.getFirstName(),user.getSecondName(),user.getInitials(),user.getGender(),user.getEmail(),
                user.getResidence(),user.getContact(),user.getRoles().stream().findFirst().get().getName().toString(),user.getDepartment().getName(),user.getDesignation(),
                user.getSupervisor1(),user.getSupervisor2(),user.getJoinDate(),user.getConfirmDate(), user.getResignDate(), user.getStatus(),user.getAnnual(),
                user.getCasual(),user.getMedical());

            LOGGER.info(">>> Successfully get employee "+user.getId()+". (By user ==> "+userId+")");
            return ResponseEntity.ok(new ApiResponse(true, employee));
        } catch(Exception e) {
            LOGGER.error(">>> Unable to get one employee. (By user ==> "+userId+")", e.getMessage());
            return ResponseEntity.ok(new ApiResponse(false, "Unable to get employee"));
        }    
    }

    public ResponseEntity<?> searchSupervisors( Long userId ) {

        try { 
            List<User> users = userRepository.findAll();
            List<Supervisor> supervisors = new ArrayList<Supervisor>();

            for (User user : users) {
                if(user.getRoles().stream().findFirst().get().getName().toString().equals("Supervisor")){
                    Supervisor supervisor = new Supervisor(user.getId(),user.getFirstName(),user.getSecondName(),user.getEmail(),
                    user.getDepartment().getName());
                    supervisors.add(supervisor);
                }
            }
            LOGGER.info(">>> Successfully get supervisors. (By user ==> "+userId+")");
            return ResponseEntity.ok(new ApiResponse(true, supervisors));
        } catch(Exception e) {
            LOGGER.error(">>> Unable to get supervisors. (By user ==> "+userId+")", e.getMessage());
            return ResponseEntity.ok(new ApiResponse(false, "Unable to get supervisors"));
        } 
    }

}