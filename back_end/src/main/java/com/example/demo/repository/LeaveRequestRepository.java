package com.example.demo.repository;

import java.util.List;
import com.example.demo.model.LeaveRequest;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Integer>{

	List<LeaveRequest> findByStatus(String pending);

	List<LeaveRequest> findByUser(User user);
    
}