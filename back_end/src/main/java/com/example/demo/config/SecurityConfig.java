package com.example.demo.config;

import com.example.demo.security.CustomUserDetailsService;
import com.example.demo.security.JwtAuthenticationEntryPoint;
import com.example.demo.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                    .and()
                .csrf()
                    .disable()
                .exceptionHandling()
                    .authenticationEntryPoint(unauthorizedHandler)
                    .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .authorizeRequests()
                    .antMatchers("/",
                        "/favicon.ico",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js")
                        .permitAll()
                    .antMatchers(HttpMethod.GET, "/api/users/**")
                        .permitAll()
                    .antMatchers( "/api/auth/signin", "/api/auth/forgot", "/api/auth/forgot_password", "/api/auth/first_login_password")
                        .permitAll() 
                    .antMatchers("/api/auth/signup", "/api/auth/correct", "/api/auth/change_password", "/api/auth/info")
                        .hasAnyAuthority("Supervisor", "Permanent", "Probation", "Intern", "Contract", "Admin")    
                    .antMatchers("/calender/all")
                        .hasAnyAuthority("Supervisor", "Permanent", "Probation", "Intern", "Contract", "Admin") 
                    .antMatchers("/calender/set_date")
                        .hasAuthority("Admin") 
                    .antMatchers("/contact/**")
                        .permitAll()
                    .antMatchers("/department/all")
                        .hasAnyAuthority("Supervisor", "Permanent", "Probation", "Intern", "Contract", "Admin") 
                    .antMatchers("/leave_count/summery/**")
                        .permitAll()
                    .antMatchers("/leave_count/profile")
                        .hasAnyAuthority("Supervisor", "Admin")
                    .antMatchers("/leave_count/profile/**")
                        .hasAuthority("Admin")    
                    .antMatchers("/leave/find/**", "/leave/request", "/leave/absent")
                        .permitAll()
                    .antMatchers("/leave/all", "/leave/pending", "/leave/delete/**", "/leave/approve/**", "/leave/reject/**")
                        .hasAnyAuthority("Supervisor", "Admin")    
                    .antMatchers("/leave_type/all")
                        .permitAll()
                    .antMatchers("/user/resign", "/user/update/**", "/user/supervisor", "/role/all", "/user/all")
                        .hasAuthority("Admin")
                    .antMatchers("/user/all")
                        .hasAnyAuthority("Supervisor", "Admin")
                    .antMatchers("/user/duty_cover", "/user/get/**", "/user/name")
                        .hasAnyAuthority("Supervisor", "Permanent", "Probation", "Intern", "Contract", "Admin") 
                    .antMatchers("/pdf/**")
                        .permitAll()
                    .antMatchers("/old_tables/**")
                        .permitAll()
                    .antMatchers("/login", "/first_login", "/forgot", "/history", "/dashboard",
                                 "/request_leave", "/change_password", "/contact_number", "/collect_lieu", "/own_pending",
                            "/manage_employee", "/leave_calender", "/view_profile", "/get_employee", "/leave_history", "/pending_leaves", "/pending_lieu", "/error404")
                        .permitAll()           
                    .anyRequest()
                        .authenticated();

        // Add our custom JWT security filter
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    }
}

// "Supervisor", "Permanent", "Probation", "Intern", "Contract", "Admin"