package com.example.demo.model;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "hrms_lieu_leaves")
public class LieuLeave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private Date date;

    @Column(name = "period")
    private Integer period;

    @Column(name = "project")
    private String project;

    @Column(name = "works_done", length = 1000)
    private String worksDone;

    @Column(name = "status")
    private Integer status;          // 0 - pending, 1 - approved, 2 - rejected

    @Column(name = "request_at")
    private Date requestAt;

    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn(name = "request_by")
    private User employee;

    @Column(name = "checked_at")
    private Date checkedAt;

    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn(name = "checked_by")
    private User checkedBy;
    
    public LieuLeave() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getRequestAt() {
        return requestAt;
    }

    public void setRequestAt(Date requestAt) {
        this.requestAt = requestAt;
    }

    public Date getCheckedAt() {
        return checkedAt;
    }

    public void setCheckedAt(Date checkedAt) {
        this.checkedAt = checkedAt;
    }

    public User getCheckedBy() {
        return checkedBy;
    }

    public void setCheckedBy(User checkedBy) {
        this.checkedBy = checkedBy;
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

    public User getEmployee() {
        return employee;
    }

    public void setEmployee(User employee) {
        this.employee = employee;
    }
}
