package com.zh.ppholic_server_demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.zh.ppholic_server_demo.model.CrmRole;
import com.zh.ppholic_server_demo.model.CrmUser;
import com.zh.ppholic_server_demo.model.User;
import com.zh.ppholic_server_demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
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
    public String memberCenter(HttpServletRequest request){
        
        HttpSession session = request.getSession();

        try {
            String userName = ((User) session.getAttribute("theUser")).getUserName();
            String token = (String) session.getAttribute("theToken");
            
            User theUser = theMemberService.getMember(userName, token);

            // pass the user object to the model
            session.setAttribute("theUser", theUser);
            
            return "member-center";
        } catch (Exception e) {

            return "access-denied";
        }
    }
    
    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("memberName") String theMemberName, Model theModel,
                                    HttpServletRequest request){

        HttpSession session = request.getSession();

        try {
            if (!((User) session.getAttribute("theUser")).getUserName().equals(theMemberName)){

                return "/access-denied";
            }

            String token = (String) session.getAttribute("theToken");
            
            // get the member from the service
            CrmUser theSiteMember = theMemberService.getCrmMember(theMemberName, token);

            // set members as a model attribute to pre-populate the form
            theModel.addAttribute("crmUser", theSiteMember);

            return "member-form";
        } catch (Exception e) {

            return "access-denied";
        }
    }

    @PostMapping("/processMemberForm")
    public String processMemberForm(@Valid @ModelAttribute("crmUser") CrmUser theSiteMember, 
                                        @RequestParam(defaultValue = "false", value = "isEmployee") boolean isEmployee,
                                        @RequestParam(defaultValue = "false", value = "isManager") boolean isManager,
                                        @RequestParam(defaultValue = "false", value = "isAdmin") boolean isAdmin,
                                        @RequestParam(defaultValue = "false", value = "isMember") boolean isMember,
                                        BindingResult theBindingResult,
                                        HttpServletRequest request){

        HttpSession session = request.getSession();

        try {
            String token = (String) session.getAttribute("theToken");
            
            // set up the roles for the CRM user
            CrmRole theCrmRole = new CrmRole(isEmployee, isManager, isAdmin, isMember);
            theSiteMember.setCrmRole(theCrmRole);
            
            // handle wrong input for form validation
            if (theBindingResult.hasErrors()) {

                return "register-form";
            }

            // create and save the member account
            theMemberService.saveUser(theSiteMember, token);

            return "redirect:/member/center";
        } catch (Exception e) {
            
            return "access-denied";
        }
    }
    
    @GetMapping("/delete")
    public String delete(HttpServletRequest request){
        
        HttpSession session = request.getSession();

        try {
            String userName = ((User) session.getAttribute("theUser")).getUserName();
            String token = (String) session.getAttribute("theToken");
            
            // delete the member from the service
            theMemberService.deleteUser(userName, token);

            // clear the user object and saved token in the session
            session.setAttribute("theUser", null);
            session.setAttribute("theToken", null);
    
            return "redirect:/";
        } catch (Exception e) {
            
            return "access-denied";
        }
    }
}
