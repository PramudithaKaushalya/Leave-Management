package com.example.demo.service;

import java.util.List;
import com.example.demo.model.LeaveType;
import com.example.demo.repository.LeaveTypeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LeaveTypeService {

    @Autowired
    private LeaveTypeRepository leaveTypeRepository;

    public List<LeaveType> getAll() {
        System.out.println("Get All Leave Types !!!");
        return leaveTypeRepository.findAll();
    }
}    