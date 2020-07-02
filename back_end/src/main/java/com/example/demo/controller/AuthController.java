package com.example.demo.controller;

import com.example.demo.exception.AppException;
import com.example.demo.model.Department;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.payload.ApiResponse;
import com.example.demo.payload.ChangePassword;
import com.example.demo.payload.JwtAuthenticationResponse;
import com.example.demo.payload.LoginRequest;
import com.example.demo.payload.SignUpRequest;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender javaMailSender;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        try {
			
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							loginRequest.getUsernameOrEmail().trim(),
							loginRequest.getPassword().trim()
					));
			
            User user = userRepository.findByUsername(loginRequest.getUsernameOrEmail().trim());
            
			if(user.getStatus().equals("Resign")){
				LOGGER.warn(">>> Inactivate user Login. (By ==> "+loginRequest.getUsernameOrEmail()+")");
				return ResponseEntity.ok(new ApiResponse(false, "Inactive account"));
			} else{
				if(user.getIsLogedIn() == true){         
					LOGGER.warn(">>> Already user Logedin. (By ==> "+loginRequest.getUsernameOrEmail()+")");
					return ResponseEntity.ok(new ApiResponse(false, "User already logedIn"));
				}
				else {

					SecurityContextHolder.getContext().setAuthentication(authentication);
					String jwt = tokenProvider.generateToken(authentication);
					
					user.setIsLogedIn(true);

					userRepository.save(user);

					LOGGER.info(">>> Successfully user Login. (By ==> "+user.getId()+") ");
					return ResponseEntity.ok(new JwtAuthenticationResponse(true, jwt));
				}
			}
		} catch ( Exception e ) {
            LOGGER.error(">>> Unable to login. (By ==> "+loginRequest.getUsernameOrEmail()+")", e.getMessage());
            return ResponseEntity.ok(new ApiResponse(false, "Unable to login"));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestHeader("Authorization") String token, @RequestBody SignUpRequest signUpRequest) {
        try{
            if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {

                String jwt = token.substring(7);
                Long userId = tokenProvider.getUserIdFromJWT(jwt);

                if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                    LOGGER.warn(">>> Email Address already in use. (By ==> "+userId+")");
                    return new ResponseEntity(new ApiResponse(false, "Email Address already in use"), HttpStatus.BAD_REQUEST);
                }

                if (userRepository.existsByUserId(signUpRequest.getUserId())) {
                    LOGGER.warn(">>> User id already in use. (By ==> "+userId+")");
                    return new ResponseEntity(new ApiResponse(false, "User id already in use"), HttpStatus.BAD_REQUEST);
                }

                String password = passwordEncoder.encode("123");

                Long d_id = new Long(signUpRequest.getDepartment());
                Department department = departmentRepository.getOne(d_id);

                User employee = new User(signUpRequest.getUserId().trim(), signUpRequest.getEmail().trim(), signUpRequest.getFirstName().trim(), 
                                        signUpRequest.getSecondName().trim(), signUpRequest.getInitials().trim(), signUpRequest.getGender(), 
                                        signUpRequest.getEmail().trim(), signUpRequest.getResidence().trim(), signUpRequest.getContact().trim(), 
                                        department, signUpRequest.getDesignation(), signUpRequest.getSupervisor1(), signUpRequest.getSupervisor2(), signUpRequest.getJoinDate(), 
                                        signUpRequest.getConfirmDate(), "Not resign", password, "Working", signUpRequest.getAnnual(), 
                                        signUpRequest.getCasual(), signUpRequest.getMedical(), signUpRequest.getImage());

                Role userRole = roleRepository.findById(signUpRequest.getRole())
                        .orElseThrow(() -> new AppException("User Role not set."));

                employee.setRoles(Collections.singleton(userRole));

                User result = userRepository.save(employee);

                URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/{username}")
                        .buildAndExpand(result.getEmail()).toUri();

                LOGGER.info(">>> Successfully create the employee. (By user ==> "+userId+")");
                return ResponseEntity.created(location).body(new ApiResponse(true, "Successfully create the employee."));
            } 
            else {
                LOGGER.warn(">>> User authentication failed");
                return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
            }
        }catch(Exception e){
            LOGGER.error(">>> Unable to create the employee", e.getMessage());
            return ResponseEntity.ok(new ApiResponse(false, "Unable to create the employee"));
        }
    }

    @GetMapping("/logout")
	public ResponseEntity<?> Logout(@RequestHeader("Authorization") String token){
		try {
            if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {
                String jwt = token.substring(7);
                Long userId = tokenProvider.getUserIdFromJWT(jwt);

                User user = userRepository.getOne(userId);
                user.setIsLogedIn(false);
                userRepository.save(user);
                LOGGER.info(">>> Successfully logout. (By ==> "+user.getId()+")");
                return ResponseEntity.ok(new ApiResponse(true, "Successfully logout"));			
            }
            else {
                LOGGER.warn(">>> User authentication failed.");
                return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
            }
		}catch (Exception e){
			LOGGER.error(">>> Unable to logout.", e.getMessage());
			return ResponseEntity.ok(new ApiResponse(false, "Unable to logout"));			
		}
    }
    
    @PostMapping("/forgot")
    public ResponseEntity<?> forgotPassword(@RequestHeader("Authorization") String token, @RequestBody LoginRequest request){

        try {
            if(StringUtils.hasText(token) && token.startsWith("Bearer ")){
                String jwt = token.substring(7);
                Long userId = tokenProvider.getUserIdFromJWT(jwt);
                User user = userRepository.findByUsername(request.getUsernameOrEmail().trim());
                String confirmCode =RandomStringUtils.randomAlphabetic(4);

                if (!userRepository.existsByEmail(request.getUsernameOrEmail().trim())) {
                    return new ResponseEntity(new ApiResponse(false, "Username incorrect!"), HttpStatus.BAD_REQUEST);
                }

                SimpleMailMessage msg = new SimpleMailMessage();
                msg.setTo(request.getUsernameOrEmail());
                msg.setSubject("Forgot credentials of VX Leave.");
                msg.setText("Hi "+user.getFirstName()+" "+user.getSecondName()+",\n\n"+
                        "You forgot your credentials and waiting for confirm code.\n"+
                        "Your requested confirm code is "+ confirmCode + ".\n\n"+
                        "Thanks. \nBest Regards"
                        );
                        
                javaMailSender.send(msg);
                user.setConfirmCode(confirmCode);
                userRepository.save(user);

                LOGGER.info(">>> Successfully send confirm code to email. (By ==> "+userId+")");
                return ResponseEntity.ok(new ApiResponse(true, "Check email for confirm code"));
            }else{
                LOGGER.warn(">>> User authentication failed");
                return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
            }    
		}catch (Exception e){
            LOGGER.error(">>> Unable to change the forgot password.", e.getMessage());
            return ResponseEntity.ok(new ApiResponse(false, "Unable to change the password"));
		}
    }

    @PostMapping("/forgot_password")
    public ResponseEntity<?> changeForgotPassword(@RequestHeader("Authorization") String token, @RequestBody ChangePassword passwords){

        try {
            if(StringUtils.hasText(token) && token.startsWith("Bearer ")){
                String jwt = token.substring(7);
                Long userId = tokenProvider.getUserIdFromJWT(jwt);

                User user = userRepository.findByUsername(passwords.getEmail().trim());
                String code = user.getConfirmCode();
        
                if(passwords.getCurrentPassword().equals(code)){
                    String password = passwordEncoder.encode(passwords.getNewPassword().trim());
                    user.setPassword(password);
                    user.setConfirmCode(null);
                    userRepository.save(user);

                    LOGGER.info(">>> Successfully change the forgot password. (By ==> "+userId+")");
                    return ResponseEntity.ok(new ApiResponse(true, "Successfully change the password"));
                }else{
                    LOGGER.warn(">>> Confirm code not match. (By ==> "+userId+")");
                    return ResponseEntity.ok(new ApiResponse(false, "Confirm code not match"));
                }
            } else {
                LOGGER.warn(">>> User authentication failed");
                return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
            }
        }catch (Exception e){
            LOGGER.error(">>> Unable to change the forgot password.", e.getMessage());
            return ResponseEntity.ok(new ApiResponse(false, "Unable to change the password"));			
        }
    }

    @GetMapping("/info")
    public ResponseEntity<?> getInfo(@RequestHeader("Authorization") String token) {
        try { 
            if(StringUtils.hasText(token) && token.startsWith("Bearer ")){
                String jwt = token.substring(7);
                Long id = tokenProvider.getUserIdFromJWT(jwt);
                return userService.searchById(id, id);
            } else {
                LOGGER.warn(">>> User authentication failed");
                return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
            }
        } catch(Exception e){
            LOGGER.error(">>> Unable to get summery of user");
            return ResponseEntity.ok(new ApiResponse(false, "Unable to get summery of user"));
        }
    }

    @PostMapping("/change_password")
    public ResponseEntity<?> getOldPassword(@RequestHeader("Authorization") String token, @RequestBody ChangePassword passwords) {

        try{

            if(StringUtils.hasText(token) && token.startsWith("Bearer ")){
                String jwt = token.substring(7);
                Long id = tokenProvider.getUserIdFromJWT(jwt);
                User user = userRepository.getOne(id);
    
                String password = passwordEncoder.encode(passwords.getNewPassword().trim());
                user.setPassword(password);
                userRepository.save(user);
                
                LOGGER.info(">>> Successfully change the password. (By ==> "+id+")");
                return ResponseEntity.ok(new ApiResponse(true, "Successfully change the password"));
            } else {
                LOGGER.warn(">>> User authentication failed");
                return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
            } 
        }catch(Exception e){
            LOGGER.error(">>> Unable to change the password");
            return ResponseEntity.ok(new ApiResponse(false, "Unable to change the password"));
        }
    }      
    
    @GetMapping("/correct")
    public ResponseEntity<?> correctToken(@RequestHeader("Authorization") String token) {
        try { 
            if(StringUtils.hasText(token) && token.startsWith("Bearer ")){
                String jwt = token.substring(7);
                Long id = tokenProvider.getUserIdFromJWT(jwt);
                LOGGER.info(">>> Direc to dashboard. (By ==> "+id+")");
                return ResponseEntity.ok(new ApiResponse(true, "Correct token"));

            } else {
                LOGGER.warn(">>> User authentication failed");
                return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
            }
        } catch(Exception e){
            LOGGER.error(">>> Unable to get summery of user");
            return ResponseEntity.ok(new ApiResponse(false, "Unable authenticate token"));
        }
    }
}