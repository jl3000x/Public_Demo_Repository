package com.zh.ppholic_server_demo.service;

import com.zh.ppholic_server_demo.model.ShoppingList;
import com.zh.ppholic_server_demo.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ShoppingListRestServiceImpl implements ShoppingListService {

    @Autowired
	RestTemplate theRestTemplate;

    @Value("${psd.api.member.url}")
    private String MEMBER_API_URL;

    @Override
    public ShoppingList getShoppingList(int theMemberId, String token) {

        HttpHeaders headers = getHeaders();
        headers.set("Authorization", token);
                
        HttpEntity<String> jwtEntity = new HttpEntity<>(headers);

        return theRestTemplate.exchange(MEMBER_API_URL + "shoppinglist/" + theMemberId, 
                                        HttpMethod.GET, jwtEntity, ShoppingList.class)
                                        .getBody();
    }
    
    @Override
    public void saveShoppingList(User theUser, String token) {
        
        String saveShoppingListURL = MEMBER_API_URL + "shoppinglist";

        HttpHeaders headers = getHeaders();
        headers.set("Authorization", token);

        HttpEntity<?> jwtEntity = new HttpEntity<>(theUser, headers);
        
        theRestTemplate.exchange(saveShoppingListURL, HttpMethod.PUT, jwtEntity, User.class);
    }
    
    private HttpHeaders getHeaders() {

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        
		return headers;
	}
}
