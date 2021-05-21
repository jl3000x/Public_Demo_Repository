package com.zh.ppholic_server_demo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.zh.ppholic_server_demo.model.Product;
import com.zh.ppholic_server_demo.service.ProductService;
import com.zh.ppholic_server_demo.util.SortUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// home controller for main entrance
@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private ProductService theProductService;

	@GetMapping("/")
	public String showHome(Model theModel) {

		// get products from the service
		List<Product> theProducts = theProductService.getSortedProducts(SortUtils.LAST_UPDATE, "");	

		// add the products to the model
		theModel.addAttribute("products", theProducts);
		
		return "home";
	}

	@GetMapping("/website-introduction")
	public String introduction() {
		
		return "website-introduction";
	}

	@GetMapping("/rest-api-introduction")
	public String restAPIIntroduction() {
		
		return "rest-api-introduction";
	}

	@PostMapping("/logout")
	public String logout(HttpServletRequest request) {

		// clear the user object and saved token in the session
		HttpSession session = request.getSession();
		session.setAttribute("theUser", null);
		session.setAttribute("theToken", null);

        return "redirect:/";
    }
}
