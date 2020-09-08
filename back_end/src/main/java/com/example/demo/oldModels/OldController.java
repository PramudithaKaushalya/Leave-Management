package com.example.demo.oldModels;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/old_tables")
public class OldController {

    @Autowired
    private OldService oldService;

    @GetMapping("/save_departments")
    public ResponseEntity<?> saveDepartments() {
            return oldService.saveDepartments();
    }

    @GetMapping("/save_leave_types")
    public ResponseEntity<?> saveLeaveTypes() {
        return oldService.saveLeaveTypes();
    }

    @GetMapping("/save_employees")
    public ResponseEntity<?> saveEmployees() {
        return oldService.saveEmployees();
    }

    @GetMapping("/save_contacts")
    public ResponseEntity<?> saveContacts() {
        return oldService.saveContacts();
    }

    @GetMapping("/save_company_leaves")
    public ResponseEntity<?> saveCompanyLeaves() {
        return oldService.saveCompanyLeaves();
    }

    @GetMapping("/save_leave_details")
    public ResponseEntity<?> saveLeaveDetails() {
        return oldService.saveLeaveDetails();
    }

    @GetMapping("/save_admin")
    public ResponseEntity<?> saveAdmin() {
        return oldService.saveAdmin();
    }
}