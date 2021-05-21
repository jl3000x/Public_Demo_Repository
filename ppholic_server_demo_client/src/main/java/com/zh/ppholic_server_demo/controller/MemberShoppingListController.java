package com.zh.ppholic_server_demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.zh.ppholic_server_demo.model.Product;
import com.zh.ppholic_server_demo.model.ShoppingList;
import com.zh.ppholic_server_demo.model.Subtotal;
import com.zh.ppholic_server_demo.model.User;
import com.zh.ppholic_server_demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/member/shoppinglist")
@Validated
public class MemberShoppingListController {

    @Autowired
    private UserService theUserService;

    @Autowired
	RestTemplate theRestTemplate;

    @Value("${psd.api.member.url}")
    private String MEMBER_API_URL;

    @GetMapping("/center")
    public String listShoppingList(HttpServletRequest request, Model theModel){

        HttpSession session = request.getSession();

        try {
            String userName = ((User) session.getAttribute("theUser")).getUserName();
            String token = (String) session.getAttribute("theToken");
    
            // get latest member information from the server
            User theUser = theUserService.getMember(userName, token);
            ShoppingList theShoppingList = theUser.getShoppingList();

            // pass the result, search word and sort index to the model
            theModel.addAttribute("theShoppingList", theShoppingList);
            theModel.addAttribute("userName", userName);
            
            return "shopping-list";
        } catch (Exception e) {

            return "access-denied";
        }
    }

    @GetMapping("/addProduct")
    public String addProduct(@RequestParam(defaultValue = "1", value = "theProductID") int theProductID,
                                @RequestParam(defaultValue = "1", value = "amount") int amount,
                                HttpServletRequest request,
                                Model theModel){

        HttpSession session = request.getSession();

        try {
            // prepare the information for jwt entity header and url
            String userName = ((User) session.getAttribute("theUser")).getUserName();
            String token = (String) session.getAttribute("theToken");
            String addProductURL = MEMBER_API_URL + "shoppinglist/addProduct/" + userName;

            // generate the header for the jwt entity
            HttpHeaders headers = getHeaders();
            headers.set("Authorization", token);

            // generate the subtotal as the body for the jwt entity
            Product theProduct = new Product();
            theProduct.setId(theProductID);
            Subtotal theSubtotal= new Subtotal(amount, theProduct);
            
            // generate the jwt entity
            HttpEntity<?> jwtEntity = new HttpEntity<>(theSubtotal, headers);

            // update and get the updated user information by exchange method and http put request
            User theUser = theRestTemplate.exchange(addProductURL, 
                                                    HttpMethod.PUT, jwtEntity, User.class).getBody();

            ShoppingList theShoppingList = theUser.getShoppingList();

            // pass the result to the model
            theModel.addAttribute("theShoppingList", theShoppingList);
            theModel.addAttribute("userName", userName);
            
            return "shopping-list";
        } catch (Exception e) {

            return "access-denied";
        }
    }
    
    @GetMapping("/discardProduct")
    public String discardProduct(@RequestParam(value = "subtotalId") int theSubtotalID,
                                @RequestParam(value = "shoppinglistId") int shoppinglistId,
                                HttpServletRequest request,
                                Model theModel){

        HttpSession session = request.getSession();

        try {
            // prepare the information for jwt entity header and url
            String userName = ((User) session.getAttribute("theUser")).getUserName();
            String token = (String) session.getAttribute("theToken");
            String discardProductURL = MEMBER_API_URL + "shoppinglist/discardProduct/" + userName;

            // generate the header for the jwt entity
            HttpHeaders headers = getHeaders();
            headers.set("Authorization", token);

            // generate the subtotal as the body for the jwt entity
            Subtotal theSubtotal= new Subtotal();
            theSubtotal.setId(theSubtotalID);
            
            // generate the jwt entity
            HttpEntity<?> jwtEntity = new HttpEntity<>(theSubtotal, headers);

            // update and get the updated user information by exchange method and http put request
            User theUser = theRestTemplate.exchange(discardProductURL, 
                                                    HttpMethod.PUT, 
                                                    jwtEntity, 
                                                    User.class)
                                            .getBody();

            ShoppingList theShoppingList = theUser.getShoppingList();

            // pass the result to the model
            theModel.addAttribute("theShoppingList", theShoppingList);
            theModel.addAttribute("userName", userName);
            
            return "shopping-list";
        } catch (Exception e) {

            return "access-denied";
        }
    }
    
    // pay for the cart
    @GetMapping("/pay")
    public String payShoppingList(HttpServletRequest request, Model theModel){
        
        HttpSession session = request.getSession();

        try {
            // prepare the information for jwt entity header and url
            String userName = ((User) session.getAttribute("theUser")).getUserName();
            String token = (String) session.getAttribute("theToken");
            String discardProductURL = MEMBER_API_URL + "shoppinglist/pay/" + userName;

            // generate the header for the jwt entity
            HttpHeaders headers = getHeaders();
            headers.set("Authorization", token);
            
            // generate the jwt entity
            HttpEntity<?> jwtEntity = new HttpEntity<>(headers);

            // update and get the updated user information by exchange method and http put request
            ShoppingList thePaidShoppingList = theRestTemplate.exchange(discardProductURL, 
                                                                        HttpMethod.GET, 
                                                                        jwtEntity, 
                                                                        ShoppingList.class)
                                                                .getBody();

            // pass the cleared shopping list and user name to the model
            theModel.addAttribute("theShoppingList", thePaidShoppingList);
            theModel.addAttribute("userName", userName);

            return "payment-confirm";
        } catch (Exception e) {

            return "access-denied";
        }
    }

    // clear the cart
    @GetMapping("/clear")
    public String clearShoppingList(@RequestParam("shoppinglistId") int shoppinglistId,
                                    HttpServletRequest request,
                                    Model theModel){

        HttpSession session = request.getSession();

        try {
            // prepare the information for jwt entity header and url
            String userName = ((User) session.getAttribute("theUser")).getUserName();
            String token = (String) session.getAttribute("theToken");
            String clearShoppingListURL = MEMBER_API_URL + "shoppinglist/clear/" + userName;

            // generate the header for the jwt entity
            HttpHeaders headers = getHeaders();
            headers.set("Authorization", token);            
            
            // generate the jwt entity
            HttpEntity<?> jwtEntity = new HttpEntity<>(headers);

            // update and get the updated user information by exchange method and http put request
            User theUser = theRestTemplate.exchange(clearShoppingListURL, 
                                    HttpMethod.GET, jwtEntity, User.class).getBody();

            ShoppingList theShoppingList = theUser.getShoppingList();

            // pass the result to the model
            theModel.addAttribute("theShoppingList", theShoppingList);
            theModel.addAttribute("userName", userName);
            
            return "shopping-list";
        } catch (Exception e) {
            
            return "access-denied";
        }
    }

    private HttpHeaders getHeaders() {

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        
		return headers;
	}
}
