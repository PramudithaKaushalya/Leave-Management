package com.example.demo.model;

import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "role")
public class Role {
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer role_id;

    @Column(name = "role_name")
    private String role_name;

    @JsonIgnore
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RolePrivilege> rolePrivileges ;
    
    @JsonIgnore
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Employee> employees ;

    public Role(){

    }

    public Role(Integer id, String name){
        this.role_id = id;
        this.role_name = name;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public Integer getRole_id() {
        return role_id;
    }

    public void setRole_id(Integer role_id) {
        this.role_id = role_id;
    }

    public List<RolePrivilege> getRolePrivileges() {
        return rolePrivileges;
    }

    public void setRolePrivileges(List<RolePrivilege> rolePrivileges) {
        this.rolePrivileges = rolePrivileges;
    }
    
}