package com.zh.ppholic_server_demo.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.zh.ppholic_server_demo.entity.Product;
import com.zh.ppholic_server_demo.entity.ShoppingList;
import com.zh.ppholic_server_demo.entity.ShoppingRecord;
import com.zh.ppholic_server_demo.entity.Subtotal;
import com.zh.ppholic_server_demo.service.ProductService;
import com.zh.ppholic_server_demo.service.ShoppingListService;
import com.zh.ppholic_server_demo.service.ShoppingRecordService;
import com.zh.ppholic_server_demo.service.SubtotalService;
import com.zh.ppholic_server_demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/member/shoppinglist")
@Validated
public class MemberShoppingListController {

    @Autowired
    private ProductService theProductService;

    @Autowired
    private SubtotalService theSubtotalService;

    @Autowired
    private ShoppingListService theShoppingListService;

    @Autowired
    private ShoppingRecordService theShoppingRecordService;

    @Autowired
    private UserService theUserService;

    @GetMapping("/center")
    public String listShoppingList(Authentication authentication, Model theModel){

        // get shoppingLists from the service by username
        String userName = authentication.getName();
		ShoppingList theShoppingList = theUserService.getUser(userName).getShoppingList();

        // pass the result, search word and sort index to the model
        theModel.addAttribute("theShoppingList", theShoppingList);
        theModel.addAttribute("userName", userName);
        
        return "shopping-list";
    }

    @GetMapping("/addProduct")
    public String addProduct(@RequestParam(defaultValue = "1", value = "theProductID") int theProductID,
                                @RequestParam(defaultValue = "1", value = "amount") int amount,
                                Authentication authentication, 
                                Model theModel){
        
        // get shoppingList from the service by user
        String userName = authentication.getName();
        ShoppingList theShoppingList = theUserService.getUser(userName).getShoppingList();
        
        Product theProduct = theProductService.getProduct(theProductID);
        
        // update the total amount of the shopping cart
        int increaseAmount = amount * theProduct.getPrice();
        theShoppingList.setTotal(theShoppingList.getTotal() + increaseAmount);
        
        // update the subtotals of the shopping cart
        Collection<Subtotal> theSubtotals = theShoppingList.getSubtotals();
        theSubtotals.add(new Subtotal(amount, theProduct));
        theShoppingList.setSubtotals(theSubtotals);    
        
        // save the updated shopping cart
        theShoppingListService.saveShoppingList(theShoppingList);

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
        int price = theSubtotal.getProduct().getPrice();
        
        // delete the subtotal by ID
        theSubtotalService.deleteSubtotal(theSubtotalID);

        // get shoppingList from the service by id
        ShoppingList theShoppingList = theShoppingListService.getShoppingList(shoppinglistId);

        // update the total amount
        int decreaseAmount = amount * price;
        theShoppingList.setTotal(theShoppingList.getTotal() - decreaseAmount);
        theShoppingListService.saveShoppingList(theShoppingList);

        // pass the updated shopping list and user name to the model
        theModel.addAttribute("theShoppingList", theShoppingList);
        theModel.addAttribute("userName", userName);

        return "shopping-list";
    }

    // pay for the cart
    @GetMapping("/pay")
    public String payShoppingList(@RequestParam(value = "shoppinglistId") int shoppinglistId,
                                    @RequestParam("userName") String userName,
                                    Model theModel){
        
        // get shoppingList from the service by id
        ShoppingList theShoppingCart = theShoppingListService.getShoppingList(shoppinglistId);
        
        // create new shoppingList for shopping record;
        ShoppingList thePaidShoppingList = new ShoppingList();
        Collection<Subtotal> thePaidSubtotals = new ArrayList<>();
        
        // get shoppingrecord from userName
        ShoppingRecord theShoppingRecord = theUserService.getUser(userName).getShoppingRecord();
        
        // copy the subtotals in the shopping cart.
        // clear all the subtotals in the shopping list in the DB.
        for (Subtotal tempSubtotal: theShoppingCart.getSubtotals()){
        
            // copy each subtotal by copy constructor
            Subtotal newSubtotal = new Subtotal(tempSubtotal);
            
            // save the new created subtotal to the DB and collection new shopping list.
            theSubtotalService.saveSubtotal(newSubtotal);
            thePaidSubtotals.add(newSubtotal);

            // delete the old subtotal in the DB.
            theSubtotalService.deleteSubtotal(tempSubtotal.getId());        
        }
        
        // clear all the subtotals in the shopping cart in the controller.
        Collection<Subtotal> theSubtotals = theShoppingCart.getSubtotals();
        theSubtotals.clear();
        theShoppingCart.setSubtotals(theSubtotals);
        
        // update the subtotal collection, total amount and date for shopping record
        thePaidShoppingList.setSubtotals(thePaidSubtotals);
        thePaidShoppingList.setLastUpdate(new Date());
        thePaidShoppingList.setTotal(theShoppingCart.getTotal());
        
        // save the new shopping list to the shopping list collection of the shopping record.
        // if there's no shopping list collection before, create a new one.
        try {
            Set<ShoppingList> theShoppingLists = theShoppingRecord.getShoppingLists();    
            theShoppingLists.add(thePaidShoppingList);
            theShoppingRecord.setShoppingLists(theShoppingLists);
        } catch (Exception e) {
            
            Set<ShoppingList> theShoppingLists = new HashSet<>();
            theShoppingLists.add(thePaidShoppingList);
            theShoppingRecord.setShoppingLists(theShoppingLists);
        }
        
        // update the total amount of the shopping record.
        int increaseAmount = thePaidShoppingList.getTotal();
        theShoppingRecord.setTotal(theShoppingRecord.getTotal() + increaseAmount);
        
        // save the shopping record in DB
        theShoppingRecordService.saveShoppingRecord(theShoppingRecord);
        
        // reset the total of the shopping cart in controller and save into the DB
        theShoppingCart.setTotal(0);
        theShoppingListService.saveShoppingList(theShoppingCart);

        // pass the cleared shopping list and user name to the model
        theModel.addAttribute("theShoppingList", thePaidShoppingList);
        theModel.addAttribute("userName", userName);

        return "payment-confirm";
    }

    // clear the cart
    @GetMapping("/clear")
    public String clearShoppingList(@RequestParam("shoppinglistId") int shoppinglistId,
                                    @RequestParam("userName") String userName,
                                    Model theModel){

        // get shoppingList from the service by id
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
