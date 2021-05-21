package com.zh.ppholic_server_demo.config;

import com.zh.ppholic_server_demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class UserSecurityConfig {
    
    @Configuration
    public static class GeneralSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private UserService theUserService;

        @Autowired
        private UserAuthenticationSuccessHandler theUserAuthenticationSuccessHandler;

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {

            return new BCryptPasswordEncoder();
        }

        @Bean
        public DaoAuthenticationProvider authenticationProvider() {

            DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
            auth.setUserDetailsService(theUserService);
            auth.setPasswordEncoder(passwordEncoder());

            return auth;
        }

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {

            return super.authenticationManagerBean();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            
            auth.authenticationProvider(authenticationProvider());
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            
            http.authorizeRequests()
                    .antMatchers("/user/systems/**").hasRole("ADMIN")
                    .antMatchers("/user/management/**").hasRole("MANAGER")
                    .antMatchers("/user/**").hasRole("EMPLOYEE")
                    .antMatchers("/product/management/**").hasRole("EMPLOYEE")
                    .antMatchers("/member/management/**").hasRole("EMPLOYEE")
                    .antMatchers("/member/**").hasRole("MEMBER")
                    .antMatchers("/api/authenticate/").permitAll()
                .and()
                .formLogin()
                    .loginPage("/showLoginPage").loginProcessingUrl("/authenticateTheUser")
                    .successHandler(theUserAuthenticationSuccessHandler)
                    .permitAll()
                .and()
                .logout()
                    .logoutSuccessUrl("/")
                    .permitAll()
                .and()
                .exceptionHandling()
                    .accessDeniedPage("/access-denied");

                http.sessionManagement().maximumSessions(5);
        }
    }
    
    @Order(1)
    @Configuration
    public static class RESTSecurityConfig extends WebSecurityConfigurerAdapter{

        @Autowired
        private UserService theUserService;

        @Autowired
        private JwtRequestFilter jwtRequestFilter;

        @Autowired
        private BCryptPasswordEncoder passwordEncoder;

        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
            // configure AuthenticationManager for the source to load user for matching credentials
            // use BCryptPasswordEncoder
            auth.userDetailsService(theUserService).passwordEncoder(passwordEncoder);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
                
                // Don't need CSRF for token acquire  
            http.csrf().disable()
                .requestMatchers()
                    .antMatchers("/api/**") 
                .and()
                .authorizeRequests()
                // dont authenticate this particular request
                    .antMatchers("/api/authenticate").permitAll()
                // all other requests need to be authenticated
                    .antMatchers("/api/member/**").authenticated()
                .and()
                // Add a filter to validate the tokens with every request
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .and()
                // make sure we use stateless session; session won't be used to store user's state.
                // disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                
                http.sessionManagement().maximumSessions(5);
        }
    }
}