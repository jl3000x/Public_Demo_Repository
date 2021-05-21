package com.zh.ppholic_server_demo.controller;

import java.util.List;

import com.zh.ppholic_server_demo.entity.Product;
import com.zh.ppholic_server_demo.service.ProductService;
import com.zh.ppholic_server_demo.util.SortUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/product")
@Validated
public class ProductController {
    
    @Autowired
    private ProductService theProductService;

    @GetMapping("/center")
    public String listProduct(Model theModel){
    
        // get products from the service
		List<Product> theProducts = theProductService.getSortedProducts(SortUtils.LAST_UPDATE, null);	

        // pass the result to the model
        theModel.addAttribute("products", theProducts);

        return "product-center";
    }

    // request from the search box, which search name shall be empty and sort index is default.
    @GetMapping("/search")
    public String searchProducts(@RequestParam(defaultValue = "8", value = "sortIndex") int sortIndex,
                                @RequestParam(defaultValue = "", value = "searchName") String theSearchName,
                                Model theModel) {
        
        // get products from the service
		List<Product> theProducts = theProductService.getSortedProducts(SortUtils.LAST_UPDATE , theSearchName);	

        // add null search word attribute to the model if no results.
        if (theProducts == null){
            theSearchName = null;
        }

        // pass the result, search word and sort index to the model
        theModel.addAttribute("products", theProducts);
        theModel.addAttribute("sortIndex", SortUtils.LAST_UPDATE);
        theModel.addAttribute("searchName", theSearchName);
        
        return "product-center";
    }

    // request from title sort, which search name and sort index can both be empty.
    @GetMapping("/show")
    public String searchAndSortProducts(@RequestParam(defaultValue = "8", value = "sortIndex") int sortIndex,
                                        @RequestParam(defaultValue = "", value = "searchName") String theSearchName,
                                        Model theModel) {
        
        // get products from the service
		List<Product> theProducts = theProductService.getSortedProducts(sortIndex, theSearchName);	

        // pass the result, search word and sort index to the model
        theModel.addAttribute("products", theProducts);
        theModel.addAttribute("sortIndex", sortIndex);
        theModel.addAttribute("searchName", theSearchName);
        
        return "product-center";
    }

    @GetMapping("/page")
    public String showProductPage(@RequestParam("productId") int theProductId, Model theModel){
        
        // get the product from the service
        Product theProduct = theProductService.getProduct(theProductId);

        // set product as a model attribute to pre-populate the form
        theModel.addAttribute("product", theProduct);

        return "product-page";
    }
}
