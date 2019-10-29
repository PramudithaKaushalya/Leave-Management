package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.Employee;
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
    public String DeleteById(@PathVariable("id") Employee id) {
        Integer emp_id = id.getEmp_id();
        // System.out.println('iiiiiiiiiiiiiiiiiiiiiiiiiiiii'+ emp_id);
        return employeeService.deleteEmployee(emp_id);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/update/{id}")
    public String updateUser(@RequestBody Employee employee, @PathVariable("id") Integer id) {
        return employeeService.updateEmployee(id, employee);
    }

    // @PostMapping(value = "/login")
    // public User login(@RequestBody User user ) {
    //     String name= user.name;
    //     return userRepository.findByName(name);
    // }


    // @GetMapping("/change/{id}")
    // public Optional<User> changePassword(@PathVariable("id") Integer id) {
    //     return userRepository.findById(id);
        
    // }

    // @PostMapping(value = "/changepassword")
    // public Boolean changeToNew(@RequestBody User user) {
    //     Integer id = user.getUser_id();
    //     String password = user.password;

    //     User data = userRepository.getOne(id);

    //     data.setPassword(password);
    //     userRepository.save(data);
    //     return true;
        
    // }
}