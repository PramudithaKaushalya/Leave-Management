package com.example.demo.model;

import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "privilege")
public class Privilege {
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "privilege_id")
    private Integer privilege_id;

    @Column(name = "privilege_name")
    private String privilege_name;

    @JsonIgnore
    @OneToMany(mappedBy = "privilege",fetch = FetchType.LAZY)
    private List<RolePrivilege> rolePrivileges ;

    public Privilege(){

    }

    public Privilege(Integer id, String privilege_name){
        this.privilege_id = id;
        this.privilege_name = privilege_name;
    }

    
    public String getPrivilege_name() {
        return privilege_name;
    }

    public void setPrivilege_name(String privilege_name) {
        this.privilege_name = privilege_name;
    }

    public Integer getPrivilege_id() {
        return privilege_id;
    }

    public void setPrivilege_id(Integer privilege_id) {
        this.privilege_id = privilege_id;
    }

    public List<RolePrivilege> getRolePrivileges() {
        return rolePrivileges;
    }

    public void setRolePrivileges(List<RolePrivilege> rolePrivileges) {
        this.rolePrivileges = rolePrivileges;
    }

    
}