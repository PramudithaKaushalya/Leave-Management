package com.example.demo.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class ErrorHandlerController implements ErrorController{

	// @Override
	// @RequestMapping("/error")
	// public ModelAndView getErrorPath() {
	// 	ModelAndView modelAndView = new ModelAndView();
    //     modelAndView.setViewName("error.html");
    //     return modelAndView;
	// }

	@RequestMapping("/error")
	public ModelAndView handleError() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error.html");
        return modelAndView;
	}
 
	@Override
	public String getErrorPath() {
		return "/error";
	}
}