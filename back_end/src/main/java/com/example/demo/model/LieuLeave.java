package com.example.demo.model;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "lieu_leaves")
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

    @Column(name = "is_approved")
    private Boolean isApproved;

    @Column(name = "request_at")
    private Date requestAt;

    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn(name = "request_by")
    private User employee;

    @Column(name = "approved_at")
    private Date approvedAt;

    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn(name = "approved_by")
    private User approvedBy;
    
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

    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    public Date getRequestAt() {
        return requestAt;
    }

    public void setRequestAt(Date requestAt) {
        this.requestAt = requestAt;
    }

    public Date getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(Date approvedAt) {
        this.approvedAt = approvedAt;
    }

    public User getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
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
