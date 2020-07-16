package com.example.demo.repository;

import java.util.List;
import com.example.demo.model.LeaveRequest;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Integer>{

	List<LeaveRequest> findByStatus(String pending);

	List<LeaveRequest> findByUser(User user);

	List<LeaveRequest> findByUserIn(List<User> users);

	List<LeaveRequest> findByStartDate(String selectedDate);

	List<LeaveRequest> findByUserAndStartDate(User user, String selectedDate);

	List<LeaveRequest> findByUserInAndStartDate(List<User> users, String selectedDate);

	List<LeaveRequest> findByStartDateIn(List<String> totalDates);

	List<LeaveRequest> findByUserAndStartDateIn(User user, List<String> totalDates);

	List<LeaveRequest> findByUserInAndStartDateIn(List<User> users, List<String> totalDates);
    
}