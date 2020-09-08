package com.example.demo.repository;

import java.util.List;

import com.example.demo.model.LieuLeave;
import com.example.demo.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LieuLeaveRepository extends JpaRepository<LieuLeave, Long> {

	List<LieuLeave> findByStatusOrderByIdDesc(Integer status);

	List<LieuLeave> findByEmployeeOrderByIdDesc(User user);

	boolean existsByEmployee(User user);

	List<LieuLeave> findByEmployeeAndStatus(User employee, Integer status);
}