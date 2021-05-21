package com.zh.ppholic_server_demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zh.ppholic_server_demo.model.APIUser;
import com.zh.ppholic_server_demo.model.ResponseToken;
import com.zh.ppholic_server_demo.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/authentication")
public class AuthenticationController {

	@Autowired
	RestTemplate theRestTemplate;
	
    @Value("${psd.api.authentication.url}")
	private String AUTHENTICATION_URL;
    
    @Value("${psd.api.member.url}")
	private String MEMBER_INFORMATION_URL;

	@GetMapping("/login")
	public String login(@RequestParam(defaultValue = "", value = "username") String username,
                        @RequestParam(defaultValue = "", value = "password")String password,
                        HttpServletRequest request,
                        Model theModel) throws JsonProcessingException {
        
        // Create then convert the user authentication object to JSON
		APIUser theAPIUser = getAuthenticationUser(username, password);
        String authenticationBody = getBody(theAPIUser);

        // create headers specifying that it is JSON request
        HttpHeaders authenticationHeaders = getHeaders();

        HttpEntity<String> authenticationEntity = new HttpEntity<>(authenticationBody, authenticationHeaders);

        // Authenticate User and get JWT
        try {
            ResponseEntity<ResponseToken> authenticationResponse = theRestTemplate.exchange(AUTHENTICATION_URL,
                                                                                            HttpMethod.POST, 
                                                                                            authenticationEntity, 
                                                                                            ResponseToken.class);  
                                                                            
            // if the authentication is successful		
            if (authenticationResponse.getStatusCode().equals(HttpStatus.OK)) {
    
                String token = "Bearer " + authenticationResponse.getBody().getToken();
    
                HttpHeaders headers = getHeaders();
                headers.set("Authorization", token);
                
                HttpEntity<String> jwtEntity = new HttpEntity<>(headers);
                
                // Use Token to get Response to confirm authentication
                ResponseEntity<String> helloResponse = theRestTemplate.exchange(MEMBER_INFORMATION_URL + username, 
                                                                                HttpMethod.GET, jwtEntity, String.class);
                
                if (helloResponse.getStatusCode().equals(HttpStatus.OK)) {
                    
                    // Create the user by data from server
                    User theUser = theRestTemplate.exchange(MEMBER_INFORMATION_URL + username, 
                                                            HttpMethod.GET, jwtEntity, User.class)
                                                    .getBody();

                    // Pass the user and token to the session.
                    HttpSession session = request.getSession();
                    session.setAttribute("theUser", theUser);
                    session.setAttribute("theToken", token);
                    
                    return "member-center";
                }
            }
    
            return "access-denied"; 
        } catch (Exception e) {
            
            return "access-denied";
        }
	}

	private APIUser getAuthenticationUser(String username, String password) {
		
        APIUser theAPIUser = new APIUser();
		theAPIUser.setUsername(username);
		theAPIUser.setPassword(password);
		
        return theAPIUser;
	}

	private HttpHeaders getHeaders() {

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        
		return headers;
	}

	private String getBody(final APIUser theAPIUser) throws JsonProcessingException {
        
		return new ObjectMapper().writeValueAsString(theAPIUser);
	}
}