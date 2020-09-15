package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*") 
@Controller
public class IndexController {
    
    @RequestMapping(value={ "", "/", "/login", "/first_login", "/forgot", "/leave_history", "/history",
                            "/dashboard", "/request_leave", "/pending_leaves",
                            "/manage_employee", "/change_password", "/view_profile", "/get_employee", 
                            "/leave_calender", "/contact_number", "/collect_lieu", "/pending_lieu", "/own_pending", "/error404"})
	public String directToIndex(){
        return "index.html";
    }
 
}