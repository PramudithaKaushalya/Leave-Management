package com.example.demo.oldModels;

import java.util.List;

import com.example.demo.model.*;
import com.example.demo.oldModels.payloads.UserPayload;
import com.example.demo.repository.LeaveTypeRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OldJDBC {
    private static final String DEPARTMENTS =  "select name, id from department order by id";
    private static final String LEAVE_TYPE =  "select id, description from leave_type order by id";
    private static final String EMPLOYEE_DETAILS =  "select id, emp_no, emp_initials, f_name, l_name, email, joined_date, confirm_date, resigned_date, case when status=1 then 'Working' else 'Resign' end as status, department_id from employee order by id";
    private static final String CONTACT_DETAILS =  "select user_id, Name, Relation, Telephone_no from emergancy_details";
    private static final String COMPANY_LEAVES =  "select date, description from company_leaves order by id";
    private static final String LEAVE_DETAILS =  "select user_id, requested_time, leave_start_time, leave_end_time, number_of_days, case when number_of_days=0.5 then 'Morning' else 'Full Day' end as start_half, leave_type, duties_covered_by, case when leave_status=1 then 'Pending' when leave_status=0 then 'Rejected' when leave_status=3 then 'Approved' when leave_status=2 then 'Deleted' end as leave_status, approved_by, approval_time, employee_comment, case when leave_status=0 then supervisor_comment else 'Not reject' end as supervisor_comment from leave_details order by id";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LeaveTypeRepository leaveTypeRepository;

    public List<Department> getDepartments() {
        return jdbcTemplate.query(DEPARTMENTS, (rs, rowNum) ->
                new Department(rs.getLong("id"), rs.getString("name")) );
    }

    public List<LeaveType> getLeaveTypes() {
        return jdbcTemplate.query(LEAVE_TYPE, (rs, rowNum) ->
                new LeaveType(rs.getInt("id"), rs.getString("description")) );
    }

    public List<UserPayload> getEmployees() {
        return jdbcTemplate.query(EMPLOYEE_DETAILS, (rs, rowNum) ->
                new UserPayload(rs.getLong("id"), rs.getString("emp_no"), rs.getString("emp_initials"), rs.getString("f_name"), rs.getString("l_name"), rs.getString("email"), rs.getString("joined_date"), rs.getString("confirm_date"), rs.getString("resigned_date"), rs.getString("status"), rs.getLong("department_id")));
    }

    public List<Contact> getContacts() {
        return jdbcTemplate.query(CONTACT_DETAILS, (rs, rowNum) ->
                new Contact(userRepository.getOne(rs.getLong("user_id")), rs.getString("Name"), rs.getString("Relation"), rs.getString("Telephone_no")));
    }

    public List<Calender> getCompanyLeaves() {
        return jdbcTemplate.query(COMPANY_LEAVES, (rs, rowNum) ->
                new Calender(rs.getString("date"), rs.getString("description")) );
    }

    public List<LeaveRequest> getLeaveDetails() {
        return jdbcTemplate.query(LEAVE_DETAILS, (rs, rowNum) ->
                new LeaveRequest(userRepository.getOne(rs.getLong("user_id")), leaveTypeRepository.getOne(rs.getInt("leave_type")), rs.getString("leave_start_time"), rs.getString("leave_end_time"), rs.getString("start_half"), "Full Day", rs.getFloat("number_of_days"), userRepository.getOne(rs.getLong("duties_covered_by")), rs.getString("employee_comment"), rs.getString("leave_status"), rs.getString("supervisor_comment"), rs.getString("requested_time"), userRepository.getOne(rs.getLong("approved_by")), rs.getString("approval_time") ) );
    }
}
