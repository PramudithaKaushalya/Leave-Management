package com.example.demo.model;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "role_privilege")
public class RolePrivilege {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @JsonBackReference
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;
    
    @JsonBackReference
    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.DETACH}, fetch = FetchType.EAGER )
    @JoinColumn(name = "privilege_id")
    private Privilege privilege;


    @Override
    public String toString() {
        return
                "id=" + id +
                ", user=" + role +
                ", favorite=" + privilege;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Privilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Privilege privilege) {
        this.privilege = privilege;
    }

}