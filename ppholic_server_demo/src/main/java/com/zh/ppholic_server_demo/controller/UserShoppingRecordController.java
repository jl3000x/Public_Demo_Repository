package com.zh.ppholic_server_demo.controller;

import com.zh.ppholic_server_demo.entity.Product;
import com.zh.ppholic_server_demo.entity.ShoppingList;
import com.zh.ppholic_server_demo.entity.ShoppingRecord;
import com.zh.ppholic_server_demo.entity.Subtotal;
import com.zh.ppholic_server_demo.service.ShoppingListService;
import com.zh.ppholic_server_demo.service.ShoppingRecordService;
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
@RequestMapping("/user/shoppingrecord")
@Validated
public class UserShoppingRecordController {

    @Autowired
    private ShoppingRecordService theShoppingRecordService;

    @Autowired
    private ShoppingListService theShoppingListService;

    @Autowired
    private SubtotalService theSubtotalService;
        
    @Autowired
    private UserService theUserService;

    @GetMapping("/center")
    public String listShoppingRecord(@RequestParam("userName") String userName, Model theModel){

        // get shoppingRecords from the service by user
		ShoppingRecord theShoppingRecord = theUserService.getUser(userName).getShoppingRecord();

        // pass the shopping record and the user name to the model
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

        // pass the shopping list and the user name to the model
        theModel.addAttribute("theShoppingList", theShoppingList);
        theModel.addAttribute("userName", userName);
        
        return "shopping-record-page";
    }

    @GetMapping("/shoppinglist/discardProduct")
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

        // get shoppingRecord from the service by name
        ShoppingRecord theShoppingRecord = theUserService.getUser(userName).getShoppingRecord();

        // update the total amount
        int decreaseAmount = amount * theProduct.getPrice();
        theShoppingList.setTotal(theShoppingList.getTotal() - decreaseAmount);
        theShoppingRecord.setTotal(theShoppingRecord.getTotal() - decreaseAmount);
        theShoppingListService.saveShoppingList(theShoppingList);
        theShoppingRecordService.saveShoppingRecord(theShoppingRecord);

        // pass the updated shopping list and user name to the model
        theModel.addAttribute("theShoppingList", theShoppingList);
        theModel.addAttribute("userName", userName);

        return "shopping-record-page";
    }

    @GetMapping("/shoppinglist/delete")
    public String deleteShoppingList(@RequestParam(value = "shoppinglistId") int shoppinglistId,
                                    @RequestParam("userName") String userName,
                                    Model theModel){

        // get shoppingLists from the service by id
        ShoppingList theShoppingList = theShoppingListService.getShoppingList(shoppinglistId);

        // calculate the update amount and delete the shopping list
        int decreaseAmount = theShoppingList.getTotal();
        theShoppingListService.deleteShoppingList(shoppinglistId);

        // get, update, save shoppingRecord from the service by name
        ShoppingRecord theShoppingRecord = theUserService.getUser(userName).getShoppingRecord();
        theShoppingRecord.setTotal(theShoppingRecord.getTotal() - decreaseAmount);
        theShoppingRecordService.saveShoppingRecord(theShoppingRecord);

        // pass the shopping record and the user name to the model
        theModel.addAttribute("theShoppingRecord", theShoppingRecord);
        theModel.addAttribute("userName", userName);
        
        return "shopping-record-center"; 
    }
}

