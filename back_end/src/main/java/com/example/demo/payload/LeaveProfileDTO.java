package com.example.demo.payload;


public class LeaveProfileDTO {

    private Long id;
    private String name;
    private Float casual;
    private Float medical;
    private Float maternity;
    private Float paternity;
    private Float annual;
    private Float lieu;
    private Float special;
    private Float coverup;
    private Float nopay;

    public LeaveProfileDTO () {}

    public LeaveProfileDTO (Long id, String name, Float casual, Float medical, Float maternity, Float paternity, Float annual, Float lieu, Float special, Float coverup, Float nopay){
        this.id = id;
        this.name = name;
        this.casual = casual;
        this.medical = medical;
        this.maternity = maternity;
        this.paternity = paternity;
        this.annual = annual;
        this.lieu = lieu;
        this.special = special;
        this.coverup = coverup;
        this.nopay = nopay;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getCasual() {
        return casual;
    }

    public void setCasual(Float casual) {
        this.casual = casual;
    }

    public Float getMedical() {
        return medical;
    }

    public void setMedical(Float medical) {
        this.medical = medical;
    }

    public Float getMaternity() {
        return maternity;
    }

    public void setMaternity(Float maternity) {
        this.maternity = maternity;
    }

    public Float getPaternity() {
        return paternity;
    }

    public void setPaternity(Float paternity) {
        this.paternity = paternity;
    }

    public Float getAnnual() {
        return annual;
    }

    public void setAnnual(Float annual) {
        this.annual = annual;
    }

    public Float getLieu() {
        return lieu;
    }

    public void setLieu(Float lieu) {
        this.lieu = lieu;
    }

    public Float getSpecial() {
        return special;
    }

    public void setSpecial(Float special) {
        this.special = special;
    }

    public Float getCoverup() {
        return coverup;
    }

    public void setCoverup(Float coverup) {
        this.coverup = coverup;
    }

    public Float getNopay() {
        return nopay;
    }

    public void setNopay(Float nopay) {
        this.nopay = nopay;
    }

    

}