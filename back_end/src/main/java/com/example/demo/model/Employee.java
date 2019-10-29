package com.example.demo.model;

import java.sql.Blob;
import java.util.List;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "employee")
public class Employee {

	@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "emp_id")
    private Integer emp_id;
    
    @Column(name = "first_name")
    private String first_name;

    @Column(name = "second_name")
    private String second_name;

    @Column(name = "initials")
    private String initials;

    @Column(name = "gender")
    private Integer gender;

    @Column(name = "email")
    private String email;

    @Column(name = "residence")
    private String residence;

    @Column(name = "contact")
    private String contact;

    
    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn(name = "role_id")
	public Role role;

    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "supervisor1")
    private String supervisor1;

    @Column(name = "supervisor2")
    private String supervisor2;

    @Column(name = "join_date")
    private String join_date;

    @Column(name = "confirm_date")
    private String confirm_date;

    @Column(name = "password")
    private String password;
    
	@Column(name = "image")
    private Blob image;

    @JsonIgnore
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LeaveRequest> leaveRequests;

    @JsonIgnore
    @OneToMany(mappedBy = "duty", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LeaveRequest> leaveDuty;

    public Employee() {

    }
    
    public Employee( Integer emp_id, String first_name, String second_name, String initials, Integer gender, String email, String residence, String contact, Role role, Department department, String supervisor1, String supervisor2, String join_date, String confirm_date, String password, Blob image) {
        this.emp_id = emp_id;
        this.first_name = first_name;
        this.second_name = second_name;
        this.initials = initials;
        this.gender = gender;
        this.email = email;
        this.residence = residence;
        this.contact = contact;
        this.role = role;
        this.department = department;
        this.supervisor1 = supervisor1;
        this.supervisor2 = supervisor2;
        this.join_date = join_date;
        this.confirm_date = confirm_date; 
        this.password = password;
        this.image = image;
	}

    public Integer getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(Integer emp_id) {
        this.emp_id = emp_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getSecond_name() {
        return second_name;
    }

    public void setSecond_name(String second_name) {
        this.second_name = second_name;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getSupervisor1() {
        return supervisor1;
    }

    public void setSupervisor1(String supervisor1) {
        this.supervisor1 = supervisor1;
    }

    public String getSupervisor2() {
        return supervisor2;
    }

    public void setSupervisor2(String supervisor2) {
        this.supervisor2 = supervisor2;
    }

    public String getJoin_date() {
        return join_date;
    }

    public void setJoin_date(String join_date) {
        this.join_date = join_date;
    }

    public String getConfirm_date() {
        return confirm_date;
    }

    public void setConfirm_date(String confirm_date) {
        this.confirm_date = confirm_date;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    
}
