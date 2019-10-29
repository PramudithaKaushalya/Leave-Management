package com.example.demo.dto;

import java.sql.Blob;
import javax.persistence.*;

public class EmployeeDTO {

    private Integer emp_id;
    private String first_name;
    private String second_name;
    private String initials;
    private Integer gender;
    private String email;
    private String residence;
    private String contact;
	private Integer role;
    private Integer department;
    private String supervisor1;
    private String supervisor2;
    private String join_date;
    private String confirm_date;
    private String password;
    private Blob image;

    public EmployeeDTO() {

    }
    
    public EmployeeDTO( Integer emp_id, String first_name, String second_name, String initials, Integer gender, String email, String residence, String contact, Integer role, Integer department, String supervisor1, String supervisor2, String join_date, String confirm_date, String password, Blob image) {
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

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Integer getDepartment() {
        return department;
    }

    public void setDepartment(Integer department) {
        this.department = department;
    }

    
}
