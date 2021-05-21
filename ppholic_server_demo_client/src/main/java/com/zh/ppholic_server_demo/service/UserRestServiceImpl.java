package com.zh.ppholic_server_demo.service;

import com.zh.ppholic_server_demo.model.CrmRole;
import com.zh.ppholic_server_demo.model.CrmUser;
import com.zh.ppholic_server_demo.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserRestServiceImpl implements UserService{

    @Autowired
	RestTemplate theRestTemplate;

    @Autowired
    BCryptPasswordEncoder theBCryptPasswordEncoder;

    @Value("${psd.api.member.url}")
    private String MEMBER_API_URL;

    @Value("${psd.api.register.url}")
    private String REGISTER_API_URL;

    @Override
    public User getMember(String username, String token){
            
        HttpHeaders headers = getHeaders();
        headers.set("Authorization", token);
                
        HttpEntity<String> jwtEntity = new HttpEntity<>(headers);

        return theRestTemplate.exchange(MEMBER_API_URL + username, 
                                        HttpMethod.GET, jwtEntity, User.class)
                                        .getBody();
    }

    @Override
    public CrmUser getCrmMember(String username, String token){
        
        // create the new CRM user
        CrmUser theCrmUser = new CrmUser();

        // acquire the user information for the new crm user
        User theUser = getMember(username, token);
        
        theCrmUser.setId(theUser.getId());
        theCrmUser.setUserName(theUser.getUserName());
        theCrmUser.setPassword(theBCryptPasswordEncoder.encode(theUser.getPassword()));
        theCrmUser.setMatchingPassword(theBCryptPasswordEncoder.encode(theUser.getPassword()));
        theCrmUser.setFirstName(theUser.getFirstName());
        theCrmUser.setLastName(theUser.getLastName());
        theCrmUser.setEmail(theUser.getEmail());
        theCrmUser.setShoppingList(theUser.getShoppingList());
        theCrmUser.setShoppingRecord(theUser.getShoppingRecord());
        theCrmUser.setCrmRole(new CrmRole(theUser.getRoles()));

        return theCrmUser;
    }

    @Override
    public String saveUser(CrmUser theCrmUser) {

        String username = theCrmUser.getUserName();
        String registerUserURL = REGISTER_API_URL + username;

        HttpHeaders headers = getHeaders();

        HttpEntity<?> jwtEntity = new HttpEntity<>(theCrmUser, headers);
        
        return theRestTemplate.exchange(registerUserURL, HttpMethod.POST, jwtEntity, String.class).getBody();
    }

    @Override
    public void saveUser(CrmUser theCrmUser, String token) {
    
        String username = theCrmUser.getUserName();
        String saveUserURL = MEMBER_API_URL + username;

        HttpHeaders headers = getHeaders();
        headers.set("Authorization", token);

        HttpEntity<?> jwtEntity = new HttpEntity<>(theCrmUser, headers);
        
        theRestTemplate.exchange(saveUserURL, HttpMethod.PUT, jwtEntity, User.class);
    }

    @Override
    public void deleteUser(String username, String token) {

        HttpHeaders headers = getHeaders();
        headers.set("Authorization", token);
                
        HttpEntity<String> jwtEntity = new HttpEntity<>(headers);

        theRestTemplate.exchange(MEMBER_API_URL + username, HttpMethod.DELETE, jwtEntity, User.class);
    }
    
    private HttpHeaders getHeaders() {

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        
		return headers;
	}
}