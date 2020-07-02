package com.example.demo.payload;

import javax.validation.constraints.*;

public class SignUpRequest {

    private String userId;

    @NotBlank
    @Size(min = 4, max = 40)
    private String firstName;

    @Size(min = 4, max = 40)
    private String secondName;

    @Size(min = 1, max = 10)
    private String initials;

    private String gender;

    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @Size(min = 1, max = 100)
    private String residence;

    @NotBlank
    @Size(min = 1, max = 10)
    private String contact;

    private Long role;

    private Integer department;

    private String designation;

    // @NotBlank
    @Size(min = 1, max = 20)
    private String supervisor1;

    // @NotBlank
    @Size(min = 1, max = 20)
    private String supervisor2;

    @NotBlank
    @Size(min = 1, max = 10)
    private String joinDate;

    @Size(min = 1, max = 10)
    private String confirmDate;

    @Size(min = 1, max = 10)
    private String status;

    private Float annual;

    private Float casual;

    private Float medical;

    private String image;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getRole() {
        return role;
    }

    public void setRole(Long role) {
        this.role = role;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public Integer getDepartment() {
        return department;
    }

    public void setDepartment(Integer department) {
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
