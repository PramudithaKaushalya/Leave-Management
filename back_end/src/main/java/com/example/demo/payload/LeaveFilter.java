package com.example.demo.payload;

public class LeaveFilter {

    private Integer selectWhose;
    private Long selectedWhose;
    private Integer selectDate;
    private String selectedDate;
    private String selectedOtherDate;

    public Integer getSelectWhose() {
        return selectWhose;
    }

    public void setSelectWhose(Integer selectWhose) {
        this.selectWhose = selectWhose;
    }

    public Long getSelectedWhose() {
        return selectedWhose;
    }

    public void setSelectedWhose(Long selectedWhose) {
        this.selectedWhose = selectedWhose;
    }

    public Integer getSelectDate() {
        return selectDate;
    }

    public void setSelectDate(Integer selectDate) {
        this.selectDate = selectDate;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public String getSelectedOtherDate() {
        return selectedOtherDate;
    }

    public void setSelectedOtherDate(String selectedOtherDate) {
        this.selectedOtherDate = selectedOtherDate;
    }

    

}