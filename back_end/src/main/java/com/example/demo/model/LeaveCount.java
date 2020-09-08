package com.example.demo.model;

import javax.persistence.*;

@Entity
@Table(name = "hrms_leave_count")
public class LeaveCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn(name = "leave_type")
    private LeaveType type;

    @Column(name = "count")
    private Float count;

    @Column(name = "pending")
    private Float pending;

    @Column(name = "year")
    private Integer year;

    public LeaveCount () {

    }

    public LeaveCount (User user, LeaveType leaveType, Float count, Float pending, Integer year) {
        this.user = user;
        this.type = leaveType;
        this.count = count;
        this.pending = pending;
        this.year = year;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getCount() {
        return count;
    }

    public void setCount(Float count) {
        this.count = count;
    }

    public LeaveType getType() {
        return type;
    }

    public void setType(LeaveType type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Float getPending() {
        return pending;
    }

    public void setPending(Float pending) {
        this.pending = pending;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}