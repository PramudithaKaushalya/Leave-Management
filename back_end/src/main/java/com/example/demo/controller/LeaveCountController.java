package com.example.demo.controller;

import java.util.List;

import com.example.demo.dto.LeaveProfileDTO;
import com.example.demo.model.Employee;
import com.example.demo.model.LeaveCount;
import com.example.demo.service.LeaveCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/leave_count")
public class LeaveCountController {

    @Autowired
    private LeaveCountService leaveCountService;

    @GetMapping("/add")
    public void add() {
        leaveCountService.addEmpty();
    }

    @GetMapping("/all")
    public List<LeaveCount> getAll() {
        return leaveCountService.getAll();
    }

    @GetMapping("/profile")
    public List<LeaveProfileDTO> getProfile() {
        return leaveCountService.getProfile();
    }

    @GetMapping("/profile/{id}")
    public LeaveProfileDTO getOneProfile(@PathVariable("id") Employee id) {
        return leaveCountService.getOneProfile(id);
    }
}