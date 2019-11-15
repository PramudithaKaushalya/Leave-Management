package com.example.demo.controller;

import java.util.List;

import com.example.demo.model.Employee;
import com.example.demo.model.LeaveRequest;
import com.example.demo.service.LeaveRequestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/leave")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestService leaveRequestService;

    @GetMapping("/all")
    public List<LeaveRequest> getAll() {
        return leaveRequestService.getAll();
    }

    @GetMapping("/find/{id}")
    public List<LeaveRequest> getOneEmployeeLeaveRequests(@PathVariable("id") Employee employee) {
        return leaveRequestService.getOneEmployeeLeaveRequests(employee);
    }

    @PostMapping(value = "/request")
    public String addEmployee(@RequestBody LeaveRequest leaveRequest) {
        return leaveRequestService.addRequest(leaveRequest);
    }

    @GetMapping("/pending")
    public List<LeaveRequest> getPending() {
        String pending = "Pending";
        return leaveRequestService.getPending(pending);
    }

    @GetMapping("/delete/{id}")
    public String DeleteById(@PathVariable("id") LeaveRequest request) {
        Integer id = request.getLeave_id();
        return leaveRequestService.deleteLeave(id);
    }

    @GetMapping("/approve/{id}")
    public String ApproveById(@PathVariable("id") LeaveRequest request) {
        return leaveRequestService.approveRequest(request);
    }
}