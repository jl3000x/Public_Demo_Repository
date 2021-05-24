package com.zh.ppholic_server_demo.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import com.zh.ppholic_server_demo.entity.Product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ProductDAOImplTest {
    
    @Autowired
    private ProductDAO theProductDAO;

    @Test
    @DisplayName("Test for ProductDAO.getProduct()")
    @Transactional
    void getProductTest() {

        System.out.println("\nStart test item: " + getClass().getName() + "\n");
        
        int[] numValid = {1,2,3,7};
        
        for (int num: numValid){

            Product testProduct = new Product();
            testProduct.setId(num);

            assertEquals(testProduct.getClass(), theProductDAO.getProduct(num).getClass());
            assertEquals(testProduct.getId(), theProductDAO.getProduct(num).getId());
        }
        
        int[] numsInvalid = {-1,-2,-3, 0};
        
        for (int num: numsInvalid){

            assertNull(theProductDAO.getProduct(num));
        }

        System.out.println("\nFinish test item: " + getClass().getName() + "\n");
    }
    
    @Test
    @DisplayName("Test for ProductDAO.getSortedProducts()")
    @Transactional
    void getSortedProductsTest (){

        System.out.println("\nStart test item: " + getClass().getName() + "\n");

        int[] theSortField = {4, 5, 6, 7, 8, 1, 2, 0, 9, 999, -1, -999};
        String[] theSearchNameValid = {"Japan", "China", "Polar", "J", "C", "S", ""};
        String[] theSearchNameInValid = {"ZZ", "123", "A1", "f2qfawv"};

        for (int num: theSortField){
            
            for (String validS: theSearchNameValid){

                assertNotEquals(0, theProductDAO.getSortedProducts(num, validS).size());
            }

            for (String invalidS: theSearchNameInValid){
                
                assertEquals(0, theProductDAO.getSortedProducts(num, invalidS).size());
            }
        }

        System.out.println("\nFinish test item: " + getClass().getName() + "\n");
    }

    @Test
    @DisplayName("Test for ProductDAO.saveProduct()")
    @Transactional
    void saveProductTest() {

        System.out.println("\nStart test item: " + getClass().getName() + "\n");

        List<Product> theProducts = new ArrayList<>();
        int[] numValid = {1,2,3,7};

        for (int num: numValid){

            theProducts.add(theProductDAO.getProduct(num));
        }

        Product tempNewProductNY = new Product();
        Product tempNewProductLA = new Product();
        Product tempNewProductBoraBora = new Product();

        tempNewProductNY.setName("NY");
        tempNewProductLA.setName("LA");
        tempNewProductBoraBora.setName("BoraBora");

        theProducts.add(tempNewProductNY);
        theProducts.add(tempNewProductLA);
        theProducts.add(tempNewProductBoraBora);

        for (Product theProduct: theProducts){

            ProductDAO theMockProductDAO = mock(ProductDAO.class);

            theMockProductDAO.saveProduct(theProduct);

            ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
            verify(theMockProductDAO).saveProduct(productCaptor.capture());
            
            Product actualProduct = productCaptor.getValue();

            assertEquals(theProduct, actualProduct);
        }

        System.out.println("\nFinish test item: " + getClass().getName() + "\n");
    }

    @Test
    @DisplayName("Test for ProductDAO.deleteProduct()")
    @Transactional
    void deleteProductTest(){

        System.out.println("\nStart test item: " + getClass().getName() + "\n");

        int[] numValid = {1,2,3,7};

        for (int theProductId: numValid){

            theProductDAO.deleteProduct(theProductId);

            Product actualProduct = theProductDAO.getProduct(theProductId);

            assertNull(actualProduct);
        }

        System.out.println("\nFinish test item: " + getClass().getName() + "\n");
    }
}
