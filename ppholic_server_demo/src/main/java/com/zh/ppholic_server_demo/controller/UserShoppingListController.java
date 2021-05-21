package com.zh.ppholic_server_demo.controller;

import java.util.Collection;

import com.zh.ppholic_server_demo.entity.Product;
import com.zh.ppholic_server_demo.entity.ShoppingList;
import com.zh.ppholic_server_demo.entity.Subtotal;
import com.zh.ppholic_server_demo.service.ShoppingListService;
import com.zh.ppholic_server_demo.service.SubtotalService;
import com.zh.ppholic_server_demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user/shoppinglist")
@Validated
public class UserShoppingListController {
    
    // need to inject the shoppingList service
    @Autowired
    private ShoppingListService theShoppingListService;

    // need to inject the subtotal service
    @Autowired
    private SubtotalService theSubtotalService;

    // need to inject the product service
    @Autowired
    private UserService theUserService;

    @GetMapping("/center")
    public String listShoppingList(@RequestParam("userName") String userName,
                                     Model theModel){

        // get shoppingList from the service by user
		ShoppingList theShoppingList = theUserService.getUser(userName).getShoppingList();

        // pass the result to the model
        theModel.addAttribute("theShoppingList", theShoppingList);
        theModel.addAttribute("userName", userName);
        
        return "shopping-list";
    }

    @GetMapping("/discardProduct")
    public String discardProduct(@RequestParam(value = "subtotalId") int theSubtotalID,
                                @RequestParam(value = "shoppinglistId") int shoppinglistId,
                                @RequestParam("userName") String userName,
                                Model theModel){
        
        Subtotal theSubtotal = theSubtotalService.getSubtotal(theSubtotalID);
        int amount = theSubtotal.getAmount();
        Product theProduct = theSubtotal.getProduct();
        
        // delete the subtotal by ID
        theSubtotalService.deleteSubtotal(theSubtotalID);

        // get shoppingLists from the service by id
        ShoppingList theShoppingList = theShoppingListService.getShoppingList(shoppinglistId);

        // update the total amount
        int decreaseAmount = amount * theProduct.getPrice();
        theShoppingList.setTotal(theShoppingList.getTotal() - decreaseAmount);
        theShoppingListService.saveShoppingList(theShoppingList);

        // pass the updated shopping list and username to the model
        theModel.addAttribute("theShoppingList", theShoppingList);
        theModel.addAttribute("userName", userName);

        return "shopping-list";
    }

    // clear the cart
    @GetMapping("/clear")
    public String clearShoppingList(@RequestParam(value = "shoppinglistId") int shoppinglistId, 
                                    @RequestParam("userName") String userName,
                                    Model theModel){

        // get shoppingLists from the service by id
        ShoppingList theShoppingList = theShoppingListService.getShoppingList(shoppinglistId);

        // clear all the subtotals in the shopping list in the DB.
        for (Subtotal tempSubtotal: theShoppingList.getSubtotals()){
            theSubtotalService.deleteSubtotal(tempSubtotal.getId());
        }

        // clear all the subtotals in the shopping list in the controller.
        Collection<Subtotal> theSubtotals = theShoppingList.getSubtotals();
        theSubtotals.clear();
        theShoppingList.setSubtotals(theSubtotals);

        // reset the total of the shopping list in controller and save into the DB
        theShoppingList.setTotal(0);
        theShoppingListService.saveShoppingList(theShoppingList);

        // pass the cleared shopping list and user name to the model
        theModel.addAttribute("theShoppingList", theShoppingList);
        theModel.addAttribute("userName", userName);
        
        return "shopping-list";
    }
}
