package com.zh.ppholic_server_demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@WebAppConfiguration()
public class ProductControllerTest {
    
    private MockMvc mockMvc;

    @Autowired
    private ProductController theProductController;

    @Autowired
    protected WebApplicationContext context;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    @DisplayName("Test for ProductController.listProduct()")
    void listProductTest() throws Exception {

        System.out.println("\nStart test item: " + getClass().getName() + "\n");

        this.mockMvc.perform(get("/product/center"))
                    .andExpect(status().isOk())
                    //.andExpect(forwardedUrl("product-center"))
                    .andExpect(view().name("product-center"))
                    .andExpect(model().attributeExists("products"))
                    .andReturn();

        System.out.println("\nFinish test item: " + getClass().getName() + "\n");
    }

    @Test
    @DisplayName("Test for ProductController.searchProducts()")
    void searchProductsTest() throws Exception {

        System.out.println("\nStart test item: " + getClass().getName() + "\n");

        int[] theSortField = {4, 5, 6, 7, 8, 1, 2, 0, 9, 999, -1, -999};
        String[] theSearchNameValid = {"Japan", "China", "Polar", "J", "C", "S", ""};
        String[] theSearchNameInValid = {"ZZ", "123", "A1", "f2qfawv"};

        for (int num: theSortField){
            
            for (String validS: theSearchNameValid){

                this.mockMvc.perform(get("/product/search?sortIndex" + num)
                            .param("searchName", validS))
                            .andExpect(view().name("product-center"))
                            .andExpect(model().attributeExists("products"))
                            .andExpect(model().attributeExists("sortIndex"))
                            .andExpect(model().attributeExists("searchName"))
                            .andExpect(status().isOk());        
            }

            for (String invalidS: theSearchNameInValid){

                this.mockMvc.perform(get("/product/search?sortIndex" + num)
                            .param("searchName", invalidS))
                            .andExpect(view().name("product-center"))
                            .andExpect(model().attributeExists("products"))
                            .andExpect(model().attributeExists("sortIndex"))
                            .andExpect(model().attributeExists("searchName"))
                            .andExpect(status().isOk());
            }
        }

        System.out.println("\nFinish test item: " + getClass().getName() + "\n");
    }

    @Test
    @DisplayName("Test for ProductController.searchAndSortProducts()")
    void searchAndSortProductsTest() throws Exception {

        System.out.println("\nStart test item: " + getClass().getName() + "\n");

        int[] theSortField = {4, 5, 6, 7, 8, 1, 2, 0, 9, 999, -1, -999};
        String[] theSearchNameValid = {"Japan", "China", "Polar", "J", "C", "S", ""};
        String[] theSearchNameInValid = {"ZZ", "123", "A1", "f2qfawv"};

        for (int num: theSortField){
            
            for (String validS: theSearchNameValid){

                this.mockMvc.perform(get("/product/show?sortIndex" + num)
                            .param("searchName", validS))
                            .andExpect(view().name("product-center"))
                            .andExpect(model().attributeExists("products"))
                            .andExpect(model().attributeExists("sortIndex"))
                            .andExpect(model().attributeExists("searchName"))
                            .andExpect(status().isOk());      
            }

            for (String invalidS: theSearchNameInValid){

                this.mockMvc.perform(get("/product/show?sortIndex" + num)
                            .param("searchName", invalidS))
                            .andExpect(view().name("product-center"))
                            .andExpect(model().attributeExists("products"))
                            .andExpect(model().attributeExists("sortIndex"))
                            .andExpect(model().attributeExists("searchName"))
                            .andExpect(status().isOk());
            }
        }

        System.out.println("\nFinish test item: " + getClass().getName() + "\n");
    }

    @Test
    @DisplayName("Test for ProductController.showProductPage()")
    void showProductPageTest() throws Exception {

        System.out.println("\nStart test item: " + getClass().getName() + "\n");

        int[] theProductIds = {4, 5, 6, 7, 8, 1, 2};
        
        for (int theProductId: theProductIds){
            
            this.mockMvc.perform(get("/product/page?productId=" + theProductId))
                        .andExpect(view().name("product-page"))
                        .andExpect(status().isOk());      
        }

        System.out.println("\nFinish test item: " + getClass().getName() + "\n");
    }
}
