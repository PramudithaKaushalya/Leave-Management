package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.LeaveType;;

public interface LeaveTypeRepository extends JpaRepository<LeaveType, Integer>{
    
}