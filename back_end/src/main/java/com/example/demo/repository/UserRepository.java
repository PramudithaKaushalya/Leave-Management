package com.example.demo.repository;

import com.example.demo.model.Department;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    List<User> findAllByOrderByUserId();

    List<User> findByStatusOrderByFirstName(String working);

//    @Query(value = "INSERT INTO hrms_users(id, user_id, username, first_name, second_name, initials, email, residence, department_id, join_date, confirm_date, resign_date, password, status, annual, casual, medical, permanent, is_loged_in) " +
//                    "values(?1, ?2, ?3, ?4, ?5 , ?6, ?7, 'No residential address', ?8, ?9, ?10, ?11, ?12, ?13, 14.0, 7.0, 7.0, 'No permanent address', false )",nativeQuery = true)
//    void saveNewEmployee(Long id, String userId, String username, String firstName, String secondName, String initials, String email, Long departmentId, String joinDate, String confirmDate, String resignDate, String password, String status);
//
//    @Query(value = "INSERT INTO user_roles(user_id, role_id) values(?1, ?2)",nativeQuery = true)
//    void saveNewEmployeeAndRole(Long userId, Long roleId);

}