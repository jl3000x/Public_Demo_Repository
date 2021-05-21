package com.zh.ppholic_server_demo.controller;

import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import com.zh.ppholic_server_demo.entity.User;
import com.zh.ppholic_server_demo.model.CrmRole;
import com.zh.ppholic_server_demo.model.CrmUser;
import com.zh.ppholic_server_demo.service.UserService;
import com.zh.ppholic_server_demo.util.SortUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
@Validated
public class UserController {
    
    @Autowired
    private UserService theUserService;

    private Logger logger = Logger.getLogger(getClass().getName());

    @GetMapping("/center")
    public String userCenter(HttpServletRequest request, Authentication authentication){
        
        String userName = authentication.getName();

        User theUser = theUserService.getUser(userName);

        HttpSession session = request.getSession();
        
        session.setAttribute("user", theUser);

        return "user-center";
    }

    @GetMapping("/management")
	public String showManagement(HttpServletRequest request, Authentication authentication) {

        String userName = authentication.getName();

        User theUser = theUserService.getUser(userName);

        HttpSession session = request.getSession();
        
        session.setAttribute("user", theUser);
		
		return "management";
	}

    @GetMapping("/management/list")
    public String listUsers(Model theModel){
    
        // get users from the service
		List<User> theUsers = theUserService.getSortedUsers(SortUtils.LAST_NAME, null);	

        // add the users to the model
        theModel.addAttribute("users", theUsers);

        return "list-user";
    }

    // request from the search box, which search name cannot be empty and sort index is default.
    @GetMapping("/management/search")
    public String searchUsers(@Valid @RequestParam("theSearchName")
                                @NotEmpty(message = "Shall not be empty.") String theSearchName, Model theModel){
        
        // get users from the service
        List<User> theUsers = theUserService.getSortedUsers(SortUtils.USER_NAME, theSearchName);

        // add null search word attribute to the model if no results.
        if (theUsers == null){
            theSearchName = null;
        }

        // pass the result, search word and sort index to the model
        theModel.addAttribute("users", theUsers);
        theModel.addAttribute("sortIndex", SortUtils.USER_NAME);
        theModel.addAttribute("searchName", theSearchName);
        
        return "list-user";
    }

    // request from title sort, which search name and sort index can both be empty.
    @GetMapping("/management/show")
    public String searchAndSortUsers (@RequestParam("sortIndex") int sortIndex,
                                        @RequestParam("searchName") String theSearchName, Model theModel) {

        // get users from the service
        List<User> theUsers = theUserService.getSortedUsers(sortIndex, theSearchName);

        // pass the result, search word and sort index to the model
        theModel.addAttribute("users", theUsers);
        theModel.addAttribute("sortIndex", sortIndex);
        theModel.addAttribute("searchName", theSearchName);

        return "list-user";
    }
    
    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("userName") String theUserName, Model theModel,
                                    Authentication authentication){

        // Only manager itself or manager is allowed to update member information
        if (!authentication.getName().equals(theUserName) &&
            !authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"))){

            return "/access-denied";
        }
        
        // get the user from the service
        CrmUser theCrmUser = theUserService.getCrmUser(theUserName);

        // set users a a model attribute to pre-populate the form
        theModel.addAttribute("crmUser", theCrmUser);

        return "user-form";
    }

    @PostMapping("/processUserForm")
    public String processUserForm(@Valid @ModelAttribute("crmUser") CrmUser theCrmUser, 
                                        @RequestParam(defaultValue = "false", value = "isEmployee") boolean isEmployee,
                                        @RequestParam(defaultValue = "false", value = "isManager") boolean isManager,
                                        @RequestParam(defaultValue = "false", value = "isAdmin") boolean isAdmin,
                                        @RequestParam(defaultValue = "false", value = "isMember") boolean isMember,
                                        BindingResult theBindingResult){
        
        // set up the roles for the CRM user
        CrmRole theCrmRole = new CrmRole(isEmployee, isManager, isAdmin, isMember);
        theCrmUser.setCrmRole(theCrmRole);

        String userName = theCrmUser.getUserName();
        logger.info("Processing user update form for: " + userName);

        // handle wrong input for form validation
        if (theBindingResult.hasErrors()) {
            
            return "register-form";
        }

        // save the user account
        theUserService.saveUser(theCrmUser);

        logger.info("Successfully update the user.");

        return "redirect:/user/center";
    }
    
    @GetMapping("/delete")
    public String delete(@RequestParam("userName") String theUserName){
        
        // delete the user from the service
        theUserService.deleteUser(theUserName);

        return "redirect:/user/management/list";
    }
	
	@GetMapping("/management/systems")
	public String showSystems(HttpServletRequest request, Authentication authentication) {

        String userName = authentication.getName();

        User theUser = theUserService.getUser(userName);

        HttpSession session = request.getSession();
        
        session.setAttribute("user", theUser);
		
		return "systems";
	}
}
