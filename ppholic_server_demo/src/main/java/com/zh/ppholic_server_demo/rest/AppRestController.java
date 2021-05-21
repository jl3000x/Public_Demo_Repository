package com.zh.ppholic_server_demo.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.zh.ppholic_server_demo.entity.Product;
import com.zh.ppholic_server_demo.entity.ShoppingList;
import com.zh.ppholic_server_demo.entity.ShoppingRecord;
import com.zh.ppholic_server_demo.entity.Subtotal;
import com.zh.ppholic_server_demo.entity.User;
import com.zh.ppholic_server_demo.model.CrmUser;
import com.zh.ppholic_server_demo.service.ProductService;
import com.zh.ppholic_server_demo.service.ShoppingListService;
import com.zh.ppholic_server_demo.service.ShoppingRecordService;
import com.zh.ppholic_server_demo.service.SubtotalService;
import com.zh.ppholic_server_demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AppRestController {

    @Autowired
    private ProductService theProductService;

	@Autowired
	private SubtotalService theSubtotalService;

    @Autowired
    private ShoppingListService theShoppingListService;

    @Autowired
    private ShoppingRecordService theShoppingRecordService;

	@Autowired
	private UserService theMemberService;

	private Logger logger = Logger.getLogger(getClass().getName());

	@GetMapping("/product/{productId}")
	public Product getProduct(@PathVariable("productId") int productId) {

		return theProductService.getProduct(productId);
	}

	@GetMapping("/product/sorted_search/{theKey}")
	public List<Product> getSortedProducts(@PathVariable("theKey") String theKey) {

		String[] keyArr = theKey.split("_");

        int sortIndex = Integer.parseInt(keyArr[0]);
        String theSearchName = "";

        if (keyArr.length > 1) {
            theSearchName = keyArr[1];
        }

		return theProductService.getSortedProducts(sortIndex, theSearchName);	
	}

	@GetMapping("/member/{userName}")
	public User getMember(@PathVariable("userName") String userName) {

		return theMemberService.getUser(userName);
	}

	@PostMapping("/register/{userName}")
	public String registerMember(@RequestBody CrmUser theInputCrmUser, @PathVariable("userName") String memberName) {
		
        logger.info("Processing registration form for: " + memberName);

        // check the database whether the member existed
        // return error string to API client
        User existedMember = theMemberService.getMember(memberName);
        if (existedMember != null) {

            logger.warning("Member name already exists.");
            
            return "registrationError";
        }

        // create member account
		try {
			theMemberService.saveUser(theInputCrmUser);
        	logger.info("Successfully create the member.");

        	return "registrationSuccess";
		} catch (Exception e) {

			return "registrationError";
		}
	}

	@PutMapping("/member/{userName}")
	public User updateMember(@RequestBody CrmUser theInputCrmUser, @PathVariable("userName") String userName) {
		
		// save the member account
		theMemberService.saveUser(theInputCrmUser);

		// return the updated member to request
		return theMemberService.getUser(userName);
	}

	@DeleteMapping("/member/{userName}")
	public void deleteMember(@PathVariable("userName") String userName) {
		
		theMemberService.deleteUser(userName);
	}

	@PutMapping("/member/shoppinglist/addProduct/{userName}")
	public User addProduct(@RequestBody Subtotal theSubtotal, @PathVariable("userName") String userName){

		// get shoppingList from the service by username
		ShoppingList theShoppingList = theMemberService.getUser(userName).getShoppingList();

		// get the updated information from the passed-in new subtotal
        Product theProduct = theProductService.getProduct(theSubtotal.getProduct().getId());
		int amount = theSubtotal.getAmount();

		// update the total amount of the shopping cart
        int increaseAmount = amount * theProduct.getPrice();
        theShoppingList.setTotal(theShoppingList.getTotal() + increaseAmount);
        
		// update the subtotals of the shopping cart
		Collection<Subtotal> theSubtotals = theShoppingList.getSubtotals();
        theSubtotals.add(new Subtotal(amount, theProduct));
        theShoppingList.setSubtotals(theSubtotals);    

		// save the updated shopping cart
        theShoppingListService.saveShoppingList(theShoppingList);

		return theMemberService.getUser(userName);
	}

	@PutMapping("/member/shoppinglist/discardProduct/{userName}")
	public User discardProduct(@RequestBody Subtotal theRequestSubtotal, @PathVariable("userName") String userName){

		// cache the information for the subtotal to delete.
		int theSubtotalID = theRequestSubtotal.getId();
		Subtotal theSubtotal = theSubtotalService.getSubtotal(theSubtotalID);
        int amount = theSubtotal.getAmount();
        int price = theSubtotal.getProduct().getPrice();
        
        // delete the subtotal by ID
        theSubtotalService.deleteSubtotal(theSubtotalID);

		// get shoppingList from the service by username
		ShoppingList theShoppingList = theMemberService.getUser(userName).getShoppingList();

        // update the total amount
        int decreaseAmount = amount * price;
        theShoppingList.setTotal(theShoppingList.getTotal() - decreaseAmount);

		// save the updated shopping cart
        theShoppingListService.saveShoppingList(theShoppingList);

		return theMemberService.getUser(userName);
	}

	// pay for the cart
    @GetMapping("/member/shoppinglist/pay/{userName}")
    public ShoppingList payShoppingList(@PathVariable("userName") String userName){
        
        // get shoppingList from the service by id
        ShoppingList theShoppingCart = theMemberService.getUser(userName).getShoppingList();
        
        // create new shoppingList for shopping record;
        ShoppingList thePaidShoppingList = new ShoppingList();
        Collection<Subtotal> thePaidSubtotals = new ArrayList<>();
        
        // get shoppingrecord from userName
        ShoppingRecord theShoppingRecord = theMemberService.getUser(userName).getShoppingRecord();
        
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
        return thePaidShoppingList;
    }

	// clear the cart
    @GetMapping("/member/shoppinglist/clear/{userName}")
    public User clearShoppingList(@PathVariable("userName") String userName){

        // get shoppingList from the service by id
		int shoppinglistId = theMemberService.getUser(userName).getShoppingList().getId();
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
        
        return theMemberService.getUser(userName);
    }
}
