package com.example.demo.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @JsonIgnore
    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String end_date;

    @Column(name = "start_half")
    private String startHalf;

    @Column(name = "end_half")
    private String endHalf;
    
    @Column(name = "number_of_leave_days")
    private Float number_of_leave_days;

    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn(name = "duties_covered_by")
    private User duty;

    @Column(name = "special_notes")
    private String special_notes;

    @Column(name = "status")
    private String status;

    @Column(name = "reject")
    private String reject;

    @Column(name = "added_on")
    private String formatDateTime;
    
    @JsonIgnore
    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn(name = "checked_by")
    private User checkBy;

    @Column(name = "checked_on")
    private String checkTime;

    public LeaveRequest(){

    }

    public LeaveRequest(LeaveType leave_type, String start_date, String end_date, String startHalf, 
                        String endHalf, Float number_of_leave_days, User duties_covered_by, String special_notes, String status, 
                        String reject, String formatDateTime, User checkBy, String checkTime){
        
        this.leave_type = leave_type;
        this.startDate = start_date;
        this.end_date = end_date;
        this.startHalf = startHalf;
        this.endHalf = endHalf;
        this.number_of_leave_days = number_of_leave_days;
        this.duty = duties_covered_by;
        this.special_notes = special_notes;
        this.status = status;
        this.reject = reject;
        this.formatDateTime = formatDateTime;
        this.checkBy = checkBy;
        this.checkTime = checkTime;                    
    }

    public LeaveRequest(LeaveType leave_type, String start_date, String end_date, String startHalf, 
                        String endHalf, Float number_of_leave_days, String special_notes, String status, 
                        String reject, String formatDateTime, User checkBy, String checkTime){
        
        this.leave_type = leave_type;
        this.startDate = start_date;
        this.end_date = end_date;
        this.startHalf = startHalf;
        this.endHalf = endHalf;
        this.number_of_leave_days = number_of_leave_days;
        this.special_notes = special_notes;
        this.status = status;
        this.reject = reject;
        this.formatDateTime = formatDateTime;
        this.checkBy = checkBy;
        this.checkTime = checkTime;                    
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

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public Float getNumber_of_leave_days() {
        return number_of_leave_days;
    }

    public void setNumber_of_leave_days(Float number_of_leave_days) {
        this.number_of_leave_days = number_of_leave_days;
    }

    public String getSpecial_notes() {
        return special_notes;
    }

    public void setSpecial_notes(String special_notes) {
        this.special_notes = special_notes;
    }

    public User getDuty() {
        return duty;
    }

    public void setDuty(User duty) {
        this.duty = duty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFormatDateTime() {
        return formatDateTime;
    }

    public void setFormatDateTime(String formatDateTime) {
        this.formatDateTime = formatDateTime;
    }

    public String getReject() {
        return reject;
    }

    public void setReject(String reject) {
        this.reject = reject;
    }

    public String getStartHalf() {
        return startHalf;
    }

    public void setStartHalf(String startHalf) {
        this.startHalf = startHalf;
    }

    public String getEndHalf() {
        return endHalf;
    }

    public void setEndHalf(String endHalf) {
        this.endHalf = endHalf;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getCheckBy() {
        return checkBy;
    }

    public void setCheckBy(User checkBy) {
        this.checkBy = checkBy;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    
}