package com.example.demo.repository;

import com.example.demo.model.Department;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    List<User> findByIdIn(List<Long> userIds);

    User findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

	User getOne(Long id);

	User findByFirstName(String supervisor);

	List<User> findByStatus(String string);

	boolean existsByUserId(String userId);

	List<User> findByDepartment(Department department);

	List<User> findByRoles(Role role);
}
