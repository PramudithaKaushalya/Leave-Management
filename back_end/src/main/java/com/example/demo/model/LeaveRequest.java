package com.example.demo.model;

import javax.persistence.*;

@Entity
@Table(name = "request_leave")
public class LeaveRequest {
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leave_id")
    private Integer leave_id;

    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn(name = "leave_type")
    private LeaveType leave_type;

    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn(name = "emp_id")
    private Employee employee;

    @Column(name = "start_date")
    private String start_date;

    @Column(name = "end_date")
    private String end_date;

    @Column(name = "number_of_leave_days")
    private Integer number_of_leave_days;

    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn(name = "duties_covered_by")
    private Employee duty;

    @Column(name = "special_notes")
    private String special_notes;

    public LeaveRequest(){

    }

    public LeaveRequest(Integer leave_id, LeaveType leave_type, Integer half_day, String start_date, String end_date, Integer number_of_leave_days, Employee duties_covered_by, String special_notes){
        this.leave_id = leave_id;
        this.leave_type = leave_type;
        this.start_date = start_date;
        this.end_date = end_date;
        this.number_of_leave_days = number_of_leave_days;
        this.duty = duties_covered_by;
        this.special_notes = special_notes;
    }

    public Integer getLeave_id() {
        return leave_id;
    }

    public void setLeave_id(Integer leave_id) {
        this.leave_id = leave_id;
    }

    public LeaveType getLeave_type() {
        return leave_type;
    }

    public void setLeave_type(LeaveType leave_type) {
        this.leave_type = leave_type;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public Integer getNumber_of_leave_days() {
        return number_of_leave_days;
    }

    public void setNumber_of_leave_days(Integer number_of_leave_days) {
        this.number_of_leave_days = number_of_leave_days;
    }

    public String getSpecial_notes() {
        return special_notes;
    }

    public void setSpecial_notes(String special_notes) {
        this.special_notes = special_notes;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Employee getDuty() {
        return duty;
    }

    public void setDuty(Employee duty) {
        this.duty = duty;
    }
}