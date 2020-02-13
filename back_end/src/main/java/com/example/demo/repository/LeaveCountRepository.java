package com.example.demo.repository;

import java.util.List;
import com.example.demo.model.LeaveCount;
import com.example.demo.model.LeaveType;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveCountRepository extends JpaRepository<LeaveCount, Integer>{


	boolean existsByType(LeaveType type);

	boolean existsByUserAndType(User emp, LeaveType casual);

	LeaveCount findByUserAndType(User emp, LeaveType casual);

	boolean existsByUser(User employee);

	List<LeaveCount> findByUser(User employee);
    
}