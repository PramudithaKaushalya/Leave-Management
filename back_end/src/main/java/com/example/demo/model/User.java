package com.example.demo.model;

import com.example.demo.model.audit.DateAudit;
import java.sql.Blob;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "users",  uniqueConstraints = {
    @UniqueConstraint(columnNames = {
        "email"
    }),
    @UniqueConstraint(columnNames = {
        "username"
    })
})
public class User extends DateAudit{

	@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "user_id")
    private String userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "second_name")
    private String secondName;

    @Column(name = "initials")
    private String initials;

    @Column(name = "username")
    private String username;

    @Column(name = "gender")
    private String gender;

    @Column(name = "email")
    private String email;

    @Column(name = "residence")
    private String residence;

    @Column(name = "contact")
    private String contact;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "designation")
    private String designation;

    @Column(name = "supervisor1")
    private String supervisor1;

    @Column(name = "supervisor2")
    private String supervisor2;

    @Column(name = "join_date")
    private String joinDate;

    @Column(name = "confirm_date")
    private String confirmDate;

    @Column(name = "resign_date")
    private String resignDate;

    @Column(name = "password")
    private String password;
    
	@Column(name = "image")
    private String image;

    @Column(name = "status")
    private String status;

    @Column(name = "annual")
    private Float annual;

    @Column(name = "casual")
    private Float casual;

    @Column(name = "medical")
    private Float medical;

    @Column(name = "confirm_code")
    private String confirmCode;

    @Column(name = "isLogedIn")
    private Boolean isLogedIn=false;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LeaveRequest> leaveRequests;

    @JsonIgnore
    @OneToMany(mappedBy = "duty", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LeaveRequest> leaveDuty;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LeaveCount> leaveCounts;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Contact> contacts;

    @JsonIgnore
    @OneToMany(mappedBy = "checkBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LeaveRequest> checkBy;

    @JsonIgnore
    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Calender> createdBy;

    public User() {

    }
    
    public User(String userId, String username, String first_name, String second_name, String initials, String gender, String email, 
                String residence, String contact, Department department, String designation, String supervisor1, String supervisor2, String join_date, 
                String confirm_date, String resignDate, String password, String status, Float annual, Float casual, Float medical, String image) {
        
        this.userId = userId;
        this.username = username;
        this.firstName = first_name;
        this.secondName = second_name;
        this.initials = initials;
        this.gender = gender;
        this.email = email;
        this.residence = residence;
        this.contact = contact;
        this.department = department;
        this.designation = designation;
        this.supervisor1 = supervisor1;
        this.supervisor2 = supervisor2;
        this.joinDate = join_date;
        this.confirmDate = confirm_date; 
        this.resignDate = resignDate;
        this.password = password;
        this.status = status;
        this.annual = annual;
        this.casual = casual;
        this.medical = medical;
        this.image = image;
	}

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<LeaveRequest> getLeaveRequests() {
        return leaveRequests;
    }

    public void setLeaveRequests(List<LeaveRequest> leaveRequests) {
        this.leaveRequests = leaveRequests;
    }

    public List<LeaveRequest> getLeaveDuty() {
        return leaveDuty;
    }

    public void setLeaveDuty(List<LeaveRequest> leaveDuty) {
        this.leaveDuty = leaveDuty;
    }

    public List<LeaveCount> getLeaveCounts() {
        return leaveCounts;
    }

    public void setLeaveCounts(List<LeaveCount> leaveCounts) {
        this.leaveCounts = leaveCounts;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public Float getAnnual() {
        return annual;
    }

    public void setAnnual(Float annual) {
        this.annual = annual;
    }

    public Float getCasual() {
        return casual;
    }

    public void setCasual(Float casual) {
        this.casual = casual;
    }

    public Float getMedical() {
        return medical;
    }

    public void setMedical(Float medical) {
        this.medical = medical;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public String getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(String confirmDate) {
        this.confirmDate = confirmDate;
    }

    public String getConfirmCode() {
        return confirmCode;
    }

    public void setConfirmCode(String confirmCode) {
        this.confirmCode = confirmCode;
    }

    public List<LeaveRequest> getCheckBy() {
        return checkBy;
    }

    public void setCheckBy(List<LeaveRequest> checkBy) {
        this.checkBy = checkBy;
    }

    public List<Calender> getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(List<Calender> createdBy) {
        this.createdBy = createdBy;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getResignDate() {
        return resignDate;
    }

    public void setResignDate(String resignDate) {
        this.resignDate = resignDate;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Boolean getIsLogedIn() {
        return isLogedIn;
    }

    public void setIsLogedIn(Boolean isLogedIn) {
        this.isLogedIn = isLogedIn;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    
}
