package com.zh.ppholic_server_demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.zh.ppholic_server_demo.model.ShoppingList;
import com.zh.ppholic_server_demo.model.ShoppingRecord;
import com.zh.ppholic_server_demo.model.User;
import com.zh.ppholic_server_demo.service.ShoppingListService;
import com.zh.ppholic_server_demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/member/shoppingrecord")
@Validated
public class MemberShoppingRecordController {

    @Autowired
    private ShoppingListService theShoppingListService;
    
    @Autowired
    private UserService theUserService;

    @GetMapping("/center")
    public String listShoppingRecord(HttpServletRequest request, Model theModel){

        HttpSession session = request.getSession();

        try {
            String userName = ((User) session.getAttribute("theUser")).getUserName();
            String token = (String) session.getAttribute("theToken");
            
            User theUser = theUserService.getMember(userName, token);
            ShoppingRecord theShoppingRecord = theUser.getShoppingRecord();

            // pass the result to the model
            theModel.addAttribute("theShoppingRecord", theShoppingRecord);
            theModel.addAttribute("userName", userName);
            
            return "shopping-record-center";
        } catch (Exception e) {

            return "access-denied";
        }
    }

    @GetMapping("/shoppinglist/center")
    public String showShoppingList(@RequestParam("shoppinglistId") int shoppinglistId,
                                    HttpServletRequest request,
                                    Model theModel){

        HttpSession session = request.getSession();

        try {
            String userName = ((User) session.getAttribute("theUser")).getUserName();
            String token = (String) session.getAttribute("theToken");
            
            ShoppingList theShoppingList = theShoppingListService.getShoppingList(shoppinglistId, token);

            // pass the result to the model
            theModel.addAttribute("theShoppingList", theShoppingList);
            theModel.addAttribute("userName", userName);
            
            return "shopping-record-page";                                                   
        } catch (Exception e) {

            return "access-denied";
        }
    }
}