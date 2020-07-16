package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*") 
@Controller
public class IndexController {
    
    @RequestMapping(value={ "", "/", "/login", "/forgot", "/leave_history", "/history", 
                            "/dashboard", "/add_employee", "/request_leave", "/pending_leaves", 
                            "/manage_employee", "/change_password", "/view_profile", "/get_employee", 
                            "/leave_calender", "/contact_number", "/attendence", "/one_attendence"})
	public String getBranches(){
        return "index.html";
    }
 
}