package com.example.demo.repository;

import com.example.demo.model.LeaveRequest;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Integer>{
    
}