package com.example.demo.payload;

public class LeaveCountDTO {

    private String type;
    private Float entitlement;
    private Float utilized;
    private Float remaining;

    public LeaveCountDTO(){}

    public LeaveCountDTO(String type, Float entitlement, Float utilized, Float remaining){
        this.type = type;
        this.entitlement = entitlement;
        this.utilized = utilized;
        this.remaining = remaining;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Float getEntitlement() {
        return entitlement;
    }

    public void setEntitlement(Float entitlement) {
        this.entitlement = entitlement;
    }

    public Float getUtilized() {
        return utilized;
    }

    public void setUtilized(Float utilized) {
        this.utilized = utilized;
    }

    public Float getRemaining() {
        return remaining;
    }

    public void setRemaining(Float remaining) {
        this.remaining = remaining;
    }

    
}
