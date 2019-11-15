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
    @JoinColumn(name = "emp_id")
    private Employee employee;

    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn(name = "leave_type")
    private LeaveType type;

    @Column(name = "count")
    private Float count;

    public LeaveCount () {

    }

    public LeaveCount (Employee employee, LeaveType leaveType, Float count) {
        this.employee = employee;
        this.type = leaveType;
        this.count = count;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
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

    
}    