package com.example.demo.repository;

import java.util.List;
import com.example.demo.model.LeaveCount;
import com.example.demo.model.LeaveType;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveCountRepository extends JpaRepository<LeaveCount, Integer>{

    boolean existsByUserAndTypeAndYear(User emp, LeaveType type, Integer year);

	LeaveCount findByUserAndTypeAndYear(User emp, LeaveType type, int year);

	List<LeaveCount> findByUserAndYear(User employee, int year);

	boolean existsByUserAndYear(User employee, int year);

	List<LeaveCount> findByYear(int year);

    boolean existsByUserAndType(User employee, LeaveType leaveType);

	LeaveCount findByUserAndType(User employee, LeaveType leaveType);
}