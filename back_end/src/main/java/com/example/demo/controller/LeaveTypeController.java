package com.example.demo.controller;

import java.util.List;

import com.example.demo.model.LeaveType;
import com.example.demo.service.LeaveTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/leave_type")
public class LeaveTypeController {

    @Autowired
    private LeaveTypeService leaveTypeService;

    @GetMapping("/all")
    public List<LeaveType> getAll() {
        return leaveTypeService.getAll();
    }
}