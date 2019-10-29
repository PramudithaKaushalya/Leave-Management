package com.example.demo.model;

import javax.persistence.*;

@Entity
@Table(name = "leave_count")
public class LeaveCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "casual")
    private Integer casual;

    @Column(name = "medical")
    private Integer medical;

    @Column(name = "maternity")
    private Integer maternity;

    @Column(name = "paternity")
    private Integer paternity;

    @Column(name = "annual")
    private Integer annual;

    @Column(name = "lieu")
    private Integer lieu;

    @Column(name = "special")
    private Integer special;

    @Column(name = "coverup")
    private Integer coverup;

    @Column(name = "nopay")
    private Integer nopay;

    public LeaveCount () {

    }

    public LeaveCount (Integer id, Integer annual, Integer casual, Integer medical, Integer lieu, Integer special, Integer maternity, Integer paternity, Integer coverup, Integer nopay) {
        this.id = id;
        this.annual = annual;
        this.casual = casual;
        this.medical =medical;
        this.lieu = lieu;
        this.special = special;
        this.maternity = maternity;
        this.paternity = paternity;
        this.coverup = coverup;
        this.nopay = nopay;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCasual() {
        return casual;
    }

    public void setCasual(Integer casual) {
        this.casual = casual;
    }

    public Integer getMedical() {
        return medical;
    }

    public void setMedical(Integer medical) {
        this.medical = medical;
    }

    public Integer getMaternity() {
        return maternity;
    }

    public void setMaternity(Integer maternity) {
        this.maternity = maternity;
    }

    public Integer getPaternity() {
        return paternity;
    }

    public void setPaternity(Integer paternity) {
        this.paternity = paternity;
    }

    public Integer getAnnual() {
        return annual;
    }

    public void setAnnual(Integer annual) {
        this.annual = annual;
    }

    public Integer getLieu() {
        return lieu;
    }

    public void setLieu(Integer lieu) {
        this.lieu = lieu;
    }

    public Integer getSpecial() {
        return special;
    }

    public void setSpecial(Integer special) {
        this.special = special;
    }

    public Integer getCoverup() {
        return coverup;
    }

    public void setCoverup(Integer coverup) {
        this.coverup = coverup;
    }

    public Integer getNopay() {
        return nopay;
    }

    public void setNopay(Integer nopay) {
        this.nopay = nopay;
    }
}    