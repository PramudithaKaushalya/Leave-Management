package com.example.demo.payload;

import java.util.List;

public class ApiResponse {
    private Boolean success;
    private String message;
    private List<?> list;
    private LeaveProfileDTO profile;
    private Employee employee;

    public ApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ApiResponse(Boolean success, List<?> list) {
        this.success = success;
        this.list = list;
    }

    public ApiResponse(Boolean success, LeaveProfileDTO profile) {
        this.success = success;
        this.profile= profile;
    }

    public ApiResponse(Boolean success, Employee employee) {
        this.success = success;
        this.employee= employee;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

    public LeaveProfileDTO getProfile() {
        return profile;
    }

    public void setProfile(LeaveProfileDTO profile) {
        this.profile = profile;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
