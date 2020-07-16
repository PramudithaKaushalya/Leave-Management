package com.example.demo.payload;

public class Supervisor {

    private Long id;
    private String firstName;
    private String secondName;
    private String email;
    private String department;

    public Supervisor(){}

    public Supervisor(Long id, String firstName, String secondName, String email, String department){

        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.email = email;
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
    
}
