package com.example.demo.payload;

import java.util.Date;

public class LieuLeaveDTO {

    private Long id;
    private Date date;
    private Integer period;
    private String project;
    private String worksDone;
    private Boolean isApproved;

    public LieuLeaveDTO(Date date, Integer period, String project, Boolean isApproved) {
        this.date = date;
        this.period = period;
        this.project = project;
        this.isApproved = isApproved;
    }

    public LieuLeaveDTO(Long id, Date date, Integer period, String project, String worksDone) {
        this.id = id;
        this.date = date;
        this.period = period;
        this.project = project;
        this.worksDone = worksDone;
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

    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }
}