package com.zh.ppholic_server_demo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
@RequestMapping("/member")
@Validated
public class MemberController {
    
    @Autowired
    private UserService theMemberService;

    @GetMapping("/center")
    public String memberCenter(Model theModel){
        
        return "member-center";
    }

    @GetMapping("/management/list")
    public String listMembers(Model theModel){
    
        // get members from the service
		List<User> theMembers = theMemberService.getSortedMembers(SortUtils.LAST_NAME, null);	

        // add the members to the model
        theModel.addAttribute("members", theMembers);

        return "list-member";
    }

    // controller from the search box, which search name cannot be empty and sort index is default.
    @GetMapping("/management/search")
    public String searchMembers(@Valid @RequestParam("theSearchName")
                                @NotEmpty(message = "Shall not be empty.") String theSearchName, Model theModel){
        
        // get members from the service
        List<User> theMembers = theMemberService.getSortedMembers(SortUtils.LAST_NAME,theSearchName);

        // add null search word attribute to the model if no results.
        if (theMembers == null){
            theSearchName = null;
        }

        // pass the result, search word and sort index to the model
        theModel.addAttribute("members", theMembers);
        theModel.addAttribute("sortIndex", SortUtils.USER_NAME);
        theModel.addAttribute("searchName", theSearchName);
        
        return "list-member";
    }

    @GetMapping("/management/show")
    public String searchAndSortMembers (@RequestParam("sortIndex") int sortIndex,
                                        @RequestParam("searchName") String theSearchName, Model theModel) {

        // get members from the service
        List<User> theMembers = theMemberService.getSortedMembers(sortIndex, theSearchName);

        // pass the result, search word and sort index to the model
        theModel.addAttribute("members", theMembers);
        theModel.addAttribute("sortIndex", sortIndex);
        theModel.addAttribute("searchName", theSearchName);

        return "list-member";
    }
    
    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("memberName") String theMemberName, Model theModel,
                                    Authentication authentication){
                                                
        // Only member itself or employee is allowed to update member information
        if (!authentication.getName().equals(theMemberName) &&
            !authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYEE"))){

            return "/access-denied";
        }

        // get the members from the service
        CrmUser theSiteMember = theMemberService.getCrmMember(theMemberName);

        // set members as a model attribute to pre-populate the form
        theModel.addAttribute("crmUser", theSiteMember);
 
        return "member-form";
    }

    @PostMapping("/processMemberForm")
    public String processMemberForm(@Valid @ModelAttribute("crmUser") CrmUser theSiteMember, 
                                        @RequestParam(defaultValue = "false", value = "isEmployee") boolean isEmployee,
                                        @RequestParam(defaultValue = "false", value = "isManager") boolean isManager,
                                        @RequestParam(defaultValue = "false", value = "isAdmin") boolean isAdmin,
                                        @RequestParam(defaultValue = "false", value = "isMember") boolean isMember,
                                        BindingResult theBindingResult,
                                        HttpServletRequest request, 
                                        Model theModel){

        // set up the roles for the CRM user
        CrmRole theCrmRole = new CrmRole(isEmployee, isManager, isAdmin, isMember);

        theSiteMember.setCrmRole(theCrmRole);
        
        // handle wrong input for form validation
        if (theBindingResult.hasErrors()) {

            return "register-form";
        }

        // create and save the member account
        theMemberService.saveUser(theSiteMember);

        return "redirect:/member/center";
    }
    
    @GetMapping("/delete")
    public String delete(@RequestParam("memberName") String theUserName){
        
        // delete the member by the service
        theMemberService.deleteUser(theUserName);
   
        return "redirect:/member/management/list";
    }
}