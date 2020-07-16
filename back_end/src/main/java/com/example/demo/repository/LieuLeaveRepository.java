package com.example.demo.repository;

import java.util.List;

import com.example.demo.model.LieuLeave;
import com.example.demo.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LieuLeaveRepository extends JpaRepository<LieuLeave, Long> {

	List<LieuLeave> findByIsApprovedOrderByIdDesc(boolean b);

	List<LieuLeave> findByEmployeeOrderByIdDesc(User user);

	boolean existsByEmployee(User user);

	List<LieuLeave> findByEmployeeAndIsApproved(User employee, boolean b);
}