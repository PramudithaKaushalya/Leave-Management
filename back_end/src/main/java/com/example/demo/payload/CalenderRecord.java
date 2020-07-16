package com.example.demo.payload;

public class CalenderRecord {

    private Long id;
    private String date;
    private String reason;
    private String createdBy;
    private String createdAt;
    
    public CalenderRecord() {

    }

    public CalenderRecord(Long id, String date, String reason, String createdBy, String createdAt) {
        this.id = id;
        this.date = date;
        this.reason = reason;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
