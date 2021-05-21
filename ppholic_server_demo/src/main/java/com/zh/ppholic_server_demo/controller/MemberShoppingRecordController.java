package com.zh.ppholic_server_demo.controller;

import com.zh.ppholic_server_demo.entity.ShoppingRecord;
import com.zh.ppholic_server_demo.entity.ShoppingList;
import com.zh.ppholic_server_demo.service.ShoppingListService;
import com.zh.ppholic_server_demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/member/shoppingrecord")
public class MemberShoppingRecordController {

    @Autowired
    private ShoppingListService theShoppingListService;
        
    @Autowired
    private UserService theUserService;

    @GetMapping("/center")
    public String listShoppingRecord(Authentication authentication, Model theModel){

        // get shoppingRecords from the service by user
        String userName = authentication.getName();
		ShoppingRecord theShoppingRecord = theUserService.getUser(userName).getShoppingRecord();

        // pass the result, search word and sort index to the model
        theModel.addAttribute("theShoppingRecord", theShoppingRecord);
        theModel.addAttribute("userName", userName);
        
        return "shopping-record-center";
    }

    @GetMapping("/shoppinglist/center")
    public String showShoppingList(@RequestParam("shoppinglistId") int shoppinglistId,
                                   @RequestParam("userName") String userName,
                                     Model theModel){

        // get shoppingLists from the service by user
		ShoppingList theShoppingList = theShoppingListService.getShoppingList(shoppinglistId);

        // pass the result to the model
        theModel.addAttribute("theShoppingList", theShoppingList);
        theModel.addAttribute("userName", userName);
        
        return "shopping-record-page";
    }
}
