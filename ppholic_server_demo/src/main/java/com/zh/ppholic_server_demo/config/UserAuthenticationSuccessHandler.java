package com.zh.ppholic_server_demo.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.zh.ppholic_server_demo.entity.User;
import com.zh.ppholic_server_demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class UserAuthenticationSuccessHandler implements AuthenticationSuccessHandler{
    
    @Autowired
    private UserService theUserService;

    public void onAuthenticationSuccess(HttpServletRequest request, 
                                        HttpServletResponse response, 
                                        Authentication authentication)
                            			throws IOException, ServletException {

        System.out.println("\nIn customAuthenticationSuccessHandler\n");

        String userName = authentication.getName();

        System.out.println("User: " + userName + " login successfully.");

        User theUser = theUserService.getUser(userName);

        HttpSession session = request.getSession();
        
        session.setAttribute("user", theUser);

        System.out.println("\nUser: " + theUser.getFirstName() + " login successfully.\n");

        response.sendRedirect(request.getContextPath() + "/member/center");
    }
}