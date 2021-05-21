package com.zh.ppholic_server_demo.controller;

import java.util.logging.Logger;

import javax.validation.Valid;

import com.zh.ppholic_server_demo.entity.User;
import com.zh.ppholic_server_demo.model.CrmRole;
import com.zh.ppholic_server_demo.model.CrmUser;
import com.zh.ppholic_server_demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/register")
public class MemberRegistrationController {
    
    @Autowired
    private UserService theMemberService;

    private Logger logger = Logger.getLogger(getClass().getName());

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        
        StringTrimmerEditor theStringTrimmerEditor = new StringTrimmerEditor(true);

        dataBinder.registerCustomEditor(String.class, theStringTrimmerEditor);
    }

    @GetMapping("/showMemberRegisterForm")
    public String showMemberRegistrationForm(Model theModel){

        theModel.addAttribute("crmUser", new CrmUser());

        return "register-form";
    }

    @PostMapping("/processMemberRegisterForm")
    public String processMemberRegistrationForm(@Valid @ModelAttribute("crmUser") CrmUser theSiteMember, 
                                                @RequestParam(defaultValue = "false", value = "isEmployee") boolean isEmployee,
                                                @RequestParam(defaultValue = "false", value = "isManager") boolean isManager,
                                                @RequestParam(defaultValue = "false", value = "isAdmin") boolean isAdmin,
                                                @RequestParam(defaultValue = "true", value = "isMember") boolean isMember,
                                                BindingResult theBindingResult, 
                                                Model theModel){
        
        // set up the roles for the CRM user
        CrmRole theCrmRole = new CrmRole(isEmployee, isManager, isAdmin, isMember);
        theSiteMember.setCrmRole(theCrmRole);

        String memberName = theSiteMember.getUserName();
        logger.info("Processing registration form for: " + memberName);

        // handle wrong input for form validation
        if (theBindingResult.hasErrors()) {

            return "register-form";
        }

        // check the database whether the member existed
        // back to the register form as pre state if the member existed
        User existedMember = theMemberService.getMember(memberName);
        if (existedMember != null) {

            theModel.addAttribute("crmUser", new CrmUser());
            theModel.addAttribute("registrationError", "Member name already exists.");

            logger.warning("Member name already exists.");
            
            return "register-form";
        }

        // create member account
        theMemberService.saveUser(theSiteMember);
        logger.info("Successfully create the member.");

        return "register-confirmation";
    }
}