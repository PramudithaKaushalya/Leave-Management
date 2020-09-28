package com.example.demo.controller;

import java.net.URI;
import java.util.Collections;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.validation.Valid;
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
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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

    @Value("${spring.mail.username}")
    private String mailSender;

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
                if(user.getIsFirstLogin()){
                    LOGGER.warn(">>> First login of user. (By ==> "+loginRequest.getUsernameOrEmail()+")");
                    return ResponseEntity.ok(new ApiResponse(true, "firstLogin"));
                }
				else {
					SecurityContextHolder.getContext().setAuthentication(authentication);
					String jwt = tokenProvider.generateToken(authentication);

					LOGGER.info(">>> Successfully user Login. (By ==> "+user.getId()+") ");
					return ResponseEntity.ok(new JwtAuthenticationResponse(true, jwt));
				}
			}
		} catch ( Exception e ) {
            LOGGER.error(">>> Unable to login. (By ==> "+loginRequest.getUsernameOrEmail()+")", e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestHeader("Authorization") String token, @RequestBody SignUpRequest signUpRequest) {
        try{
            if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {

                String jwt = token.substring(7);
                Long userId = tokenProvider.getUserIdFromJWT(jwt);
//
//                if (userRepository.existsByEmail(signUpRequest.getEmail())) {
//                    LOGGER.warn(">>> Email Address already in use. (By ==> "+userId+")");
//                    return ResponseEntity.ok(new ApiResponse(false, "Email Address already in use"));
//                }

                if (userRepository.existsByUserId(signUpRequest.getUserId())) {
                    LOGGER.warn(">>> User id already in use. (By ==> "+userId+")");
                    return ResponseEntity.ok(new ApiResponse(false, "User id already in use"));
                }

                // String password =RandomStringUtils.randomAlphabetic(4);
                String password = "VizuaMatix@123";
                String encodedPassword = passwordEncoder.encode(password);

                Long d_id = new Long(signUpRequest.getDepartment());
                Department department = departmentRepository.getOne(d_id);

                User employee = new User(signUpRequest.getUserId().trim(), signUpRequest.getUserId().trim(), signUpRequest.getFirstName().trim(),
                                        signUpRequest.getSecondName().trim(), signUpRequest.getInitials().trim(), signUpRequest.getGender(), 
                                        signUpRequest.getEmail().trim(), signUpRequest.getResidence().trim(), signUpRequest.getContact().trim(), 
                                        department, signUpRequest.getDesignation(), signUpRequest.getSupervisor1(), signUpRequest.getSupervisor2(), signUpRequest.getJoinDate(), 
                                        signUpRequest.getConfirmDate(), "Not resign", encodedPassword, "Working", signUpRequest.getAnnual(),
                                        signUpRequest.getCasual(), signUpRequest.getMedical(), signUpRequest.getImage(), signUpRequest.getPermanent(), signUpRequest.getDateOfBirth(),
                                        signUpRequest.getMarriageStatus(), signUpRequest.getReligion(), signUpRequest.getNic());

                Role userRole = roleRepository.findById(signUpRequest.getRole())
                        .orElseThrow(() -> new AppException("User Role not set."));

                employee.setRoles(Collections.singleton(userRole));

                Boolean success = sendPassword(signUpRequest, password);

                if(success) {
                    User result = userRepository.save(employee);
    
                    URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/{username}")
                            .buildAndExpand(result.getEmail()).toUri();
    
                    LOGGER.info(">>> Successfully create the employee. (By user ==> "+userId+")");
                    return ResponseEntity.created(location).body(new ApiResponse(true, "Successfully create the employee."));
                } else {
                    LOGGER.warn(">>> Unable to send the email to employee. (By ==> "+userId+")");
                    return ResponseEntity.ok(new ApiResponse(false, "Invalid email. Please check"));
                }
            } 
            else {
                LOGGER.warn(">>> User authentication failed");
                return ResponseEntity.ok(new ApiResponse(false, "Authentication failed"));
            }
        }catch(Exception e){
            LOGGER.error(">>> Unable to create the employee", e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(false, "Unable to create the employee"));
        }
    }


    @Value("${login.url}")
    private String hostAndPort;

    public Boolean sendPassword(SignUpRequest user, String password) {

        MimeMessagePreparator mail = mimeMessage -> {

            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            mimeMessage.setFrom(new InternetAddress(mailSender, "VX HRMS"));
            mimeMessage.setSubject("Credentials For VX HR Management System.");
            mimeMessage.setText(
                    "Hi "+user.getFirstName()+" "+user.getSecondName()+",\n\n"+
                            "Credentials for your new Account in VX HR Management System as follows. You can change your password when you logged in to your account.\n\n"+
                            "Username - "+ user.getEmail() + ".\n"+
                            "Password - "+ password + ".\n"+
                            "URL - "+ hostAndPort + "\n\n"+
                            "Thanks. \nBest Regards");
        };

        try {
            InternetAddress internetAddress = new InternetAddress(user.getEmail());
            internetAddress.validate();
            javaMailSender.send(mail);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }       
    }

    @GetMapping("/logout")
	public ResponseEntity<?> Logout(@RequestHeader("Authorization") String token){
		try {
            if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {
                String jwt = token.substring(7);
                Long userId = tokenProvider.getUserIdFromJWT(jwt);

                User user = userRepository.getOne(userId);
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
			e.printStackTrace();
			return ResponseEntity.ok(new ApiResponse(false, "Unable to logout"));			
		}
    }
    
    @PostMapping("/forgot")
    public ResponseEntity<?> forgotPassword(@RequestBody LoginRequest request){

        try {
                if (!userRepository.existsByUserId(request.getUsernameOrEmail().trim())) {
                    LOGGER.warn(">>> Incorrect employee number. (By user ==> "+request.getUsernameOrEmail()+")");
                    return ResponseEntity.ok(new ApiResponse(false, "Incorrect employee number"));
                } else {
                    User user = userRepository.findByUsername(request.getUsernameOrEmail());
                    String confirmCode =RandomStringUtils.randomAlphabetic(4);

                    MimeMessagePreparator mail = mimeMessage -> {

                        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
                        mimeMessage.setFrom(new InternetAddress(mailSender, "VX HRMS"));
                        mimeMessage.setSubject("Forgot credentials of VX Leave Management System.");
                        mimeMessage.setText(
                                "Hi "+user.getFirstName()+" "+user.getSecondName()+",\n\n"+
                                        "You forgot your credentials and waiting for confirm code.\n"+
                                        "Your requested confirm code is "+ confirmCode + ".\n\n"+
                                        "Thanks. \nBest Regards");
                    };
                    javaMailSender.send(mail);

                    user.setConfirmCode(confirmCode);
                    userRepository.save(user);

                    LOGGER.info(">>> Successfully send confirm code to email. (By ==> "+user.getId()+")");
                    return ResponseEntity.ok(new ApiResponse(true, "Check email for confirm code"));
                }

		}catch (Exception e){
            LOGGER.error(">>> Unable to change the forgot password.", e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(false, "Unable to change the password"));
		}
    }

    @PostMapping("/forgot_password")
    public ResponseEntity<?> changeForgotPassword(@RequestBody ChangePassword passwords){

        try {
            User user = userRepository.findByUsername(passwords.getEmail());

            String code = user.getConfirmCode();

            if(passwords.getCurrentPassword().trim().equals(code)){
                String password = passwordEncoder.encode(passwords.getNewPassword().trim());
                user.setPassword(password);
                user.setConfirmCode(null);
                userRepository.save(user);

                LOGGER.info(">>> Successfully change the forgot password. (By ==> "+user.getId()+")");
                return ResponseEntity.ok(new ApiResponse(true, "Successfully change the password"));
            }else{
                LOGGER.warn(">>> Confirm code not match. (By ==> "+user.getId()+")");
                return ResponseEntity.ok(new ApiResponse(false, "Confirm code not match"));
            }
        }catch (Exception e){
            LOGGER.error(">>> Unable to change the forgot password.", e.getMessage());
            e.printStackTrace();
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
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(false, "Unable to get summery of user"));
        }
    }

    @PostMapping("/change_password")
    public ResponseEntity<?> changePassword(@RequestHeader("Authorization") String token, @RequestBody ChangePassword passwords) {

        try{
            if(StringUtils.hasText(token) && token.startsWith("Bearer ")){

                String jwt = token.substring(7);
                Long id = tokenProvider.getUserIdFromJWT(jwt);
                User user = userRepository.getOne(id);

                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                user.getUsername(),
                                passwords.getCurrentPassword().trim()
                        ));
                SecurityContextHolder.getContext().setAuthentication(authentication);

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
            LOGGER.warn(">>> Current password not correct to change password");
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(false, "Current password did not match"));
        }
    }

    @PostMapping("/first_login_password")
    public ResponseEntity<?> firstLoginChangePassword(@RequestBody ChangePassword passwords) {

        try{
            String userId = passwords.getEmail();
            User user = userRepository.findByUsername(userId);

            if(user != null) {
                String password = passwordEncoder.encode(passwords.getNewPassword().trim());
                user.setPassword(password);
                user.setIsFirstLogin(false);
                userRepository.save(user);

                LOGGER.info(">>> Successfully change the first login password. (By ==> "+userId+")");
                return ResponseEntity.ok(new ApiResponse(true, "Successfully change the password"));
            } else {
                LOGGER.warn(">>> Current password not correct to change first login password");
                return ResponseEntity.ok(new ApiResponse(false, "Current password did not match"));
            }
        }catch(Exception e){
            LOGGER.error(">>> Unable to change the password");
            e.printStackTrace();
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
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponse(false, "Unable authenticate token"));
        }
    }
}