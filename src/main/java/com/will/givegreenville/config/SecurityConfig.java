package com.will.givegreenville.config;

import com.will.givegreenville.models.User;
import com.will.givegreenville.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Qualifier("dataSource")
    @Autowired
    private DataSource dataSource;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        String usersQuery = "SELECT username, password, active FROM users WHERE username = ?";
        String rolesQuery = "SELECT u.username, r.name FROM roles r INNER JOIN users u ON u.role_id = r.id WHERE u.username = ?";
        auth.jdbcAuthentication()
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/assets/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/signup", "/login", "/", "/ask", "/give", "/flash", "/results", "/detail/**").permitAll()
                    .antMatchers("/consider/**", "/create", "/createAsk", "createGive", "createFlash").hasRole("USER")
//                    .antMatchers("/consider/**", "/create", "/createAsk", "createGive", "createFlash").hasRole("ADMIN")
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .successHandler(successHandler())
                    .failureHandler(failureHandler())
                    .and()
                .logout()
                    .permitAll()
                    .logoutSuccessUrl("/login");
    }

    private AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> response.sendRedirect("/");
    }

    private AuthenticationFailureHandler failureHandler() {
        return (request, response, exception) -> {
            request.getSession().setAttribute("error", "Cannot login with credentials provided.");
            response.sendRedirect("/login");
        };
    }
}
