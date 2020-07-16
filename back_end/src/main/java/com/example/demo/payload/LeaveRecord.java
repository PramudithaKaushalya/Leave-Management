package com.example.demo.payload;

public class LeaveRecord {
    
    private Integer id;
    private String type;
    private Long userId;
    private String user;
    private String department;
    private String startDate;
    private String endDate;
    private String startHalf;
    private String endHalf;
    private Float number_of_leave_days;
    private String duty;
    private String specialNotes;
    private String status;
    private String reject;
    private String requestDateTime;
    private String checkBy;
    private String checkTime;
    private String supervisor1;
    private String supervisor2;
    
    public LeaveRecord(){

    }

    public LeaveRecord( Integer leave_id, String leave_type, Long userId, String user, String department, String start_date, String end_date, 
                        String startHalf, String endHalf, Float number_of_leave_days, String supervisor1, 
                        String supervisor2, String duties_covered_by, String special_notes, String status, 
                        String reject, String requestDateTime, String checkBy, String checkTime){
        
        this.id = leave_id;
        this.type = leave_type;
        this.userId = userId;
        this.user = user;
        this.department = department;
        this.startDate = start_date;
        this.endDate = end_date;
        this.startHalf = startHalf;
        this.endHalf = endHalf;
        this.number_of_leave_days = number_of_leave_days;
        this.supervisor1 = supervisor1;
        this.supervisor2 = supervisor2;
        this.duty = duties_covered_by;
        this.specialNotes = special_notes;
        this.status = status;
        this.reject = reject;
        this.requestDateTime= requestDateTime;
        this.checkBy = checkBy;
        this.checkTime = checkTime;
    }

    public LeaveRecord( Integer leave_id, String leave_type, Long userId, String user, String department, String start_date, String end_date, 
                        String startHalf, String endHalf, Float number_of_leave_days, String supervisor1, 
                        String supervisor2, String duties_covered_by, String special_notes, String status, 
                        String reject, String requestDateTime){
        
        this.id = leave_id;
        this.type = leave_type;
        this.userId = userId;
        this.user = user;
        this.department = department;
        this.startDate = start_date;
        this.endDate = end_date;
        this.startHalf = startHalf;
        this.endHalf = endHalf;
        this.number_of_leave_days = number_of_leave_days;
        this.supervisor1 = supervisor1;
        this.supervisor2 = supervisor2;
        this.duty = duties_covered_by;
        this.specialNotes = special_notes;
        this.status = status;
        this.reject = reject;
        this.requestDateTime = requestDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Float getNumber_of_leave_days() {
        return number_of_leave_days;
    }

    public void setNumber_of_leave_days(Float number_of_leave_days) {
        this.number_of_leave_days = number_of_leave_days;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getSpecialNotes() {
        return specialNotes;
    }

    public void setSpecialNotes(String specialNotes) {
        this.specialNotes = specialNotes;
    }

    public String getCheckBy() {
        return checkBy;
    }

    public void setCheckBy(String checkBy) {
        this.checkBy = checkBy;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getSupervisor1() {
        return supervisor1;
    }

    public void setSupervisor1(String supervisor1) {
        this.supervisor1 = supervisor1;
    }

    public String getSupervisor2() {
        return supervisor2;
    }

    public void setSupervisor2(String supervisor2) {
        this.supervisor2 = supervisor2;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRequestDateTime() {
        return requestDateTime;
    }

    public void setRequestDateTime(String requestDateTime) {
        this.requestDateTime = requestDateTime;
    }
    
}