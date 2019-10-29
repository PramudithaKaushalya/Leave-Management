package com.example.demo.repository;

import com.example.demo.model.LeaveCount;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveCountRepository extends JpaRepository<LeaveCount, Integer>{
    
}