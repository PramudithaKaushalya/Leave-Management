package com.example.demo.controller;

import java.util.List;

import com.example.demo.model.LeaveRequest;
import com.example.demo.repository.LeaveRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/leave_request")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @GetMapping("/all")
    public List<LeaveRequest> getAll() {
        return leaveRequestRepository.findAll();
    }

    @PostMapping(value = "/request")
    public void addEmployee(@RequestBody LeaveRequest leaveRequest) {
        leaveRequestRepository.save(leaveRequest);
    }
}