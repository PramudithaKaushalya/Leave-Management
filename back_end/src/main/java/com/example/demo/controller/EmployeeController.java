package com.example.demo.controller;

import java.util.List;
import java.util.Optional;
import com.example.demo.model.Employee;
import com.example.demo.model.Role;
import com.example.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/all")
    public List<Employee> getAll() {
        return employeeService.getAll();
    }

    @GetMapping("/{id}")
    public Optional<Employee> getOne(@PathVariable("id") Integer id) {
        return employeeService.searchById(id);
    }

    @PostMapping("/{id}")
    public Optional<Employee> getOneHere(@PathVariable("id") Integer id) {
        return employeeService.searchById(id);
    }

    // @GetMapping("/{name}")
    // public User getUser(@PathVariable("name") String name) {
    //     return userRepository.findByName(name);
    // }

    @PostMapping(value = "/add")
    public void addEmployee(@RequestBody Employee employee) {
        System.out.println("Employee Name" + employee.getFirst_name());
        employeeService.addEmployee(employee);
    }

    @GetMapping("/delete/{id}")
    public String DeleteById(@PathVariable("id") Integer id) {
        return employeeService.resignEmployee(id);
    }

    @PostMapping(value = "/update/{id}")
    public String updateUser(@RequestBody Employee employee, @PathVariable("id") Integer id) {
        return employeeService.updateEmployee(id, employee);
    }

    @GetMapping("/supervisor")
    public List<Employee> findSupervisors() {
        Role role = new Role();
        role.setRole_id(1);;
        role.setRole_name("Supervisor");
        return employeeService.searchSupervisors(role);
    }

    // @PostMapping(value = "/login")
    // public User login(@RequestBody User user ) {
    //     String name= user.name;
    //     return userRepository.findByName(name);
    // }


    @GetMapping("/change/{id}")
    public Optional<Employee> changePassword(@PathVariable("id") Integer id) {
        return employeeService.changePassword(id);
    }

    @PostMapping(value = "/changepassword")
    public Boolean changeToNew(@RequestBody Employee employee) {
        return employeeService.changeToNew(employee);
    }
}