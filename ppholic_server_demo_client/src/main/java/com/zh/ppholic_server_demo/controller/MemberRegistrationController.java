package com.zh.ppholic_server_demo.controller;

import java.util.logging.Logger;

import javax.validation.Valid;

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
                                                BindingResult theBindingResult, 
                                                Model theModel){
        
        // set up the roles for the CRM user
        CrmRole theCrmRole = new CrmRole(false, false, false, true);
        theSiteMember.setCrmRole(theCrmRole);

        String memberName = theSiteMember.getUserName();
        logger.info("Processing registration form for: " + memberName);

        // handle wrong input for form validation
        if (theBindingResult.hasErrors()) {

            return "register-form";
        }

        // back to prior state when server feedback register error
        try {
            String result = theMemberService.saveUser(theSiteMember);
            
            if (result.equals("registrationError")){
                
                theModel.addAttribute("crmUser", new CrmUser());
                theModel.addAttribute("registrationError", "Member name already exists.");

                return "register-form";
            }

            return "register-confirmation";
        } catch (Exception e) {
            
            theModel.addAttribute("crmUser", new CrmUser());
            theModel.addAttribute("registrationError", "Member name already exists.");

            return "register-form";
        }        
    }
}