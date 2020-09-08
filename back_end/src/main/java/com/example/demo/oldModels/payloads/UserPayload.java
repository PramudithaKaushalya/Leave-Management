package com.example.demo.oldModels.payloads;

public class UserPayload {

    private Long id;
    private String userId;
    private String initials;
    private String firstName;
    private String lastName;
    private String email;
    private String joinDate;
    private String confirmDate;
    private String resignedDate;
    private String status;
    private Long departmentId;

    public UserPayload(Long id, String userId, String initials, String firstName, String lastName, String email, String joinDate, String confirmDate, String resignedDate, String status, Long departmentId) {
        this.id = id;
        this.userId = userId;
        this.initials = initials;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.joinDate = joinDate;
        this.confirmDate = confirmDate;
        this.resignedDate = resignedDate;
        this.status = status;
        this.departmentId = departmentId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getResignedDate() {
        return resignedDate;
    }

    public void setResignedDate(String resignedDate) {
        this.resignedDate = resignedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
}

