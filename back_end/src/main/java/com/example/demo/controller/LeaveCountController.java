package com.example.demo.controller;

import com.example.demo.repository.LeaveCountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/")
public class LeaveCountController {

    @Autowired
    private LeaveCountRepository leaveCountRepository;
}