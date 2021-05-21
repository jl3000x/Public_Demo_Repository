package com.zh.ppholic_server_demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	@GetMapping("/showLoginPage")
	public String showLoginPage() {
		
		return "login";
	}
	
	@GetMapping("/access-denied")
	public String showAccessDenied() {
		
		return "access-denied";	
	}
}