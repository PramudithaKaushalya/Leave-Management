package com.example.demo.model;

import javax.persistence.*;

@Entity
@Table(name = "leave_count")
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

    public LeaveCount () {

    }

    public LeaveCount (User user, LeaveType leaveType, Float count) {
        this.user = user;
        this.type = leaveType;
        this.count = count;
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
}    