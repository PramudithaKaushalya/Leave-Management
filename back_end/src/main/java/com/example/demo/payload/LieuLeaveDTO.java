package com.example.demo.payload;

import java.util.Date;

public class LieuLeaveDTO {

    private Long id;
    private Date date;
    private Integer period;
    private String project;
    private String worksDone;
    private Integer status;
    private String employee;
    private Date requestAt;

    public LieuLeaveDTO(Date date, Integer period, String project, Integer status) {
        this.date = date;
        this.period = period;
        this.project = project;
        this.status = status;
    }

    public LieuLeaveDTO(Long id, Date date, Integer period, String project, String worksDone, String employee, Date requestAt) {
        this.id = id;
        this.date = date;
        this.period = period;
        this.project = project;
        this.worksDone = worksDone;
        this.employee = employee;
        this.requestAt = requestAt;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getWorksDone() {
        return worksDone;
    }

    public void setWorksDone(String worksDone) {
        this.worksDone = worksDone;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public Date getRequestAt() {
        return requestAt;
    }

    public void setRequestAt(Date requestAt) {
        this.requestAt = requestAt;
    }
}