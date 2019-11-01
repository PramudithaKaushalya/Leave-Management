package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.Employee;
import com.example.demo.model.Role;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> getAll() {
        System.out.println("Get All Details of Employees!!!");
        return employeeRepository.findAll();
    }

    public void addEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    public String deleteEmployee(Integer id) {
        employeeRepository.deleteById(id);
        return "Employee Deleted!!!";
    }

    public String updateEmployee(Integer id, Employee employee) {
        System.out.println("Update Employee: "+ employee);
        Employee employeeToUpdate = employeeRepository.getOne(id);
        employeeToUpdate.setFirst_name(employee.getFirst_name());
        employeeToUpdate.setSecond_name(employee.getSecond_name());
        employeeToUpdate.setInitials(employee.getInitials());
        employeeToUpdate.setGender(employee.getGender());
        employeeToUpdate.setEmail(employee.getEmail());
        employeeToUpdate.setResidence(employee.getResidence());
        employeeToUpdate.setContact(employee.getContact());
        employeeToUpdate.setRole(employee.getRole());
        employeeToUpdate.setDepartment(employee.getDepartment());
        employeeToUpdate.setSupervisor1(employee.getSupervisor1());
        employeeToUpdate.setSupervisor2(employee.getSupervisor2());
        employeeToUpdate.setJoin_date(employee.getJoin_date());
        employeeToUpdate.setConfirm_date(employee.getConfirm_date());

        employeeRepository.save(employeeToUpdate);
        return "Employee Updated!!!";
    }

    public Optional<Employee> searchById(Integer id) {
        return employeeRepository.findById(id);
    }

    public List<Employee> searchSupervisors(Role role) {
        return employeeRepository.findByRole(role);
    }

    public Optional<Employee> changePassword(Integer id) {
        return employeeRepository.findById(id);
    }

    public Boolean changeToNew(Employee employee) {
        Integer id = employee.getEmp_id();
        String password = employee.getPassword();

        Employee data = employeeRepository.getOne(id);

        data.setPassword(password);

        employeeRepository.save(data);
        return true;
        
    }
}