package com.example.demo.model;

import java.util.List;
import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "leave_type")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class LeaveType {
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leave_type_id")
    private Integer leave_type_id;

    @Column(name = "type")
    private String type;

    @JsonIgnore
    @OneToMany(mappedBy = "leave_type", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LeaveRequest> leaveRequests ;

    @JsonIgnore
    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LeaveCount> leaveCounts ;

    public LeaveType () {

    }

    public LeaveType (Integer id, String type) {
        this.leave_type_id = id;
        this.type = type;
    }

    public Integer getLeave_type_id() {
        return leave_type_id;
    }

    public void setLeave_type_id(Integer leave_type_id) {
        this.leave_type_id = leave_type_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}    