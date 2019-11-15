package com.example.demo.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import javax.validation.Valid;
import com.example.demo.config.JwtTokenUtil;
import com.example.demo.model.Employee;
import com.example.demo.model.JwtRequest;
import com.example.demo.model.JwtResponse;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.JwtUserDetailsService;

@RestController
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService userDetailsService;

	@Autowired
    private EmployeeRepository employeeRepository;

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	Environment environment;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationController.class);
	
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new JwtResponse(token));
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> login(@RequestBody JwtRequest authenticationRequest) throws Exception {

		Employee employee = employeeRepository.findByEmail(authenticationRequest.getUsername());
		System.out.println("LLLLLLLLLLLLLLLLLLLLLLLL"+ authenticationRequest.getUsername());
		
		if(employee.getStatus().equals("news")){
			return ResponseEntity.ok(new JwtResponse("First Login"));
		}else if(employee.getStatus().equals("inActive")){
			return ResponseEntity.ok(new JwtResponse("inActive"));
		}else{
			if(employee.getStatus().equals("logedIn")){
				return ResponseEntity.ok(new JwtResponse("logedIn"));
			}else{
				System.out.println("ZZZZZZZZZZZZZZZZZZZZZZZZ"+ authenticationRequest.getPassword());
				Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
						authenticationRequest.getUsername(), 
						authenticationRequest.getPassword()
					)
				);

				System.out.println("TTTTTTTTTTTTTTTTTTTTTTTT"+ authentication);
				SecurityContextHolder.getContext().setAuthentication(authentication);
				String token = jwtTokenUtil.generateJWT(authentication);

				employee.setStatus("logedIn");
				
				LOGGER.info(">>> User Login ==> "+authenticationRequest.getUsername()+" ("+employee.getFirst_name()+")");
				return ResponseEntity.ok(new JwtResponse(token));
			}
		}
	}	

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody Employee employee) {
		
		if(employeeRepository.existsByEmail(employee.getEmail())) {
			return new ResponseEntity(new JwtResponse("User already exists."), HttpStatus.BAD_REQUEST);
		}
		
		String randomPssword =RandomStringUtils.randomAlphabetic(8);
		employee.setPassword(passwordEncoder.encode(randomPssword));

		Employee result = employeeRepository.save(employee);
		URI location = ServletUriComponentsBuilder
				.fromCurrentContextPath().path("/employee/{email}")
				.buildAndExpand(result.getEmail()).toUri();
				
		MailSender(employee, randomPssword);

		Employee supervisor1 = employeeRepository.findBySupervisor1(employee.getSupervisor1());
		MailSupervisor(supervisor1, employee);

		Employee supervisor2 = employeeRepository.findBySupervisor2(employee.getSupervisor2());
		MailSupervisor(supervisor2, employee);

		return ResponseEntity.created(location).body(new JwtResponse("Employee registered successfully!"));
	}

	private void MailSender (Employee employee, String password) {
		SimpleMailMessage msg = new SimpleMailMessage();
		String url =environment.getRequiredProperty("login_url");
		// msg.setFrom(environment.getRequiredProperty("spring.mail.sender"));
		msg.setTo(employee.getEmail());
		msg.setSubject("Credentials for VX Leave System.");
		msg.setText("Hi "+employee.getFirst_name()+" "+employee.getSecond_name()+",\n"+
		"Your new account has been created to access the VX Leave Management system. Your credentials for the system are given bellow.\n\n"+ 
		"Username: "+employee.getEmail()+"\n"+
		"Password: "+password+"\n"+
		"Login Here: "+url+"\n\n\n"+
		"You are asigned to "+employee.getDepartment().getDepartment_name()+" department. \n"+
		"Your supervisors are "+employee.getSupervisor1()+" and "+employee.getSupervisor2()+".\n"+
		"Thanks. \nBest Regards"
		);

		try {
			javaMailSender.send(msg);
			LOGGER.info(">>> New User created ==> "+employee.getFirst_name()+" "+employee.getSecond_name()+" ---> "+employee.getEmail());
			LOGGER.info(">>> E-mail send to ==> "+employee.getEmail());
		}catch (Exception e){
			LOGGER.error(">>> (MailSender) ==> "+e);
		}
	}

	private void MailSupervisor (Employee supervisor, Employee employee) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(supervisor.getEmail());
		msg.setSubject("Asign to employee as supervisor.");
		msg.setText("Hi "+supervisor.getFirst_name()+" "+supervisor.getSecond_name()+",\n"+
					"You are asigned as a supervisor to following employee. \n\n"+
					"Name: "+employee.getFirst_name()+" "+employee.getSecond_name()+"\n"+
					"Role: "+employee.getRole().getRole_name()+"\n"+
					"Department: "+employee.getDepartment().getDepartment_name()+"\n"+
					"Thanks. \n Best Regards"
					);

		try {
			javaMailSender.send(msg);
			LOGGER.info(">>> E-mail send to ==> "+supervisor.getEmail());
		}catch (Exception e){
			LOGGER.error(">>> (MailSender) ==> "+e);
		}
	}

	@GetMapping("/email")
    public void sendMail() {
		SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("pramuditha.kaushalya@gmail.com");

        msg.setSubject("Testing from Spring Boot");
        msg.setText("Hello World \n Spring Boot Email");

        try{
			javaMailSender.send(msg);
		}catch (Exception e){
			System.out.println(e);
		}
    }
}