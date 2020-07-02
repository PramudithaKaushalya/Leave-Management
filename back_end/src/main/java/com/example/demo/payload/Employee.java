package com.example.demo.payload;

public class Employee {

    private Long id;
    private String userId;
    private String firstName;
    private String secondName;
    private String initials;
    private String gender;
    private String email;
    private String residence;
    private String contact;
    private String role;
    private String department;
    private String designation;
    private String supervisor1;
    private String supervisor2;
    private String joinDate;
    private String confirmDate;
    private String resignDate;
    private String status;
    private Float annual;
    private Float casual;
    private Float medical;
    private String image;

    public Employee(){}

    public Employee(Long id, String userId, String firstName, String secondName, String initials, String gender ,String email, String residence, String contact,
                    String role, String department, String designation, String supervisor1, String supervisor2, String joinDate, String confirmDate, String resignDate,
                    String status, Float annual, Float casual, Float medical, String image){

        this.userId = userId;                
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.initials = initials;
        this.gender = gender;
        this.email = email;
        this.residence = residence;
        this.contact = contact;
        this.role = role;
        this.department = department;
        this.designation = designation;
        this.supervisor1 = supervisor1;
        this.supervisor2 = supervisor2;
        this.joinDate = joinDate;
        this.confirmDate = confirmDate;
        this.resignDate = resignDate;
        this.status = status;
        this.annual = annual;
        this.casual = casual;
        this.medical = medical;
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    
}
