package com.zh.ppholic_server_demo.controller;

import java.util.List;

import com.zh.ppholic_server_demo.entity.Product;
import com.zh.ppholic_server_demo.entity.SubCategory;
import com.zh.ppholic_server_demo.service.ProductService;
import com.zh.ppholic_server_demo.service.SubCategoryService;
import com.zh.ppholic_server_demo.util.SortUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/product/management")
@Validated
public class ProductManagementController {
    
    @Autowired
    private ProductService theProductService;

    @Autowired
    private SubCategoryService theSubCategoryService;

    @GetMapping("/list")
    public String listProduct(Model theModel){
    
        // get products from the service
		List<Product> theProducts = theProductService.getSortedProducts(SortUtils.LAST_UPDATE, null);	

        // pass the result to the model
        theModel.addAttribute("products", theProducts);

        return "list-product";
    }

    // request from the search box, which search name shall be empty and sort index is default.
    @GetMapping("/search")
    public String searchProducts(@RequestParam(defaultValue = "7", value = "sortIndex") int sortIndex,
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
        
        return "list-product";
    }

    // request from title sort, which search name and sort index can both be empty.
    @GetMapping("/show")
    public String searchAndSortProducts(@RequestParam(defaultValue = "7", value = "sortIndex") int sortIndex,
                                        @RequestParam(defaultValue = "", value = "searchName") String theSearchName,
                                        Model theModel) {
        
        // get products from the service
		List<Product> theProducts = theProductService.getSortedProducts(sortIndex, theSearchName);	

        // pass the result, search word and sort index to the model
        theModel.addAttribute("products", theProducts);
        theModel.addAttribute("sortIndex", sortIndex);
        theModel.addAttribute("searchName", theSearchName);
        
        return "list-product";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model theModel){

        // create a new product and subcategory;
        Product theProduct = new Product();
        theProduct.setSubCategory(new SubCategory());

        // set blank product as a model attribute to pre-populate the form
        theModel.addAttribute("product", theProduct);

        // create and set subcategory set as a model attribute to pre-populate the form
        List<SubCategory> theSubcategories = theSubCategoryService.getAllSubCategory();
        theModel.addAttribute("subcategories", theSubcategories);

        return "product-form";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("productId") int theProductId, Model theModel){
        
        // get the product from the service
        Product theProduct = theProductService.getProduct(theProductId);

        // set product as a model attribute to pre-populate the form
        theModel.addAttribute("product", theProduct);

        // create and set subcategory set as a model attribute to pre-populate the form
        List<SubCategory> theSubcategories = theSubCategoryService.getAllSubCategory();
        theModel.addAttribute("subcategories", theSubcategories);

        return "product-form";
    }

    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute("product") Product theProduct,
                                @RequestParam("theSubCategory") int subCategory){
        
        // set the category for the project
        SubCategory theSubCategory = theSubCategoryService.getSubCategory(subCategory);
        theProduct.setSubCategory(theSubCategory);

        // save the product using the service
        theProductService.saveProduct(theProduct);
        
        return "redirect:/product/management/list";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("productId") int theProductId, Model theModel){
        
        // delete the customer from the service
        theProductService.deleteProduct(theProductId);

        return "redirect:/product/management/list";
    }
}
