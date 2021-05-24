package com.zh.ppholic_server_demo.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import com.zh.ppholic_server_demo.dao.ProductDAO;
import com.zh.ppholic_server_demo.entity.Product;
import com.zh.ppholic_server_demo.service.ProductService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class ProductServiceImplTest {

    @Autowired
    private ProductService theProductService;

    @MockBean
    private ProductDAO theProductDAO;

    private AutoCloseable theAutoCloseable;

    @BeforeEach
    void setUp() {
        theAutoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        theAutoCloseable.close();
    }

    @Test
    @DisplayName("Test for ProductService.getProduct()")
    void getProductTest() {

        System.out.println("\nStart test item: " + getClass().getName() + "\n");
        
        int[] numsValid = {1,2,3,1000};

        for (int num: numsValid){

            Product testProduct = new Product();
            testProduct.setId(num);
            Mockito.when(theProductDAO.getProduct(num)).thenReturn(testProduct);

            assertEquals(testProduct.getClass(), theProductService.getProduct(num).getClass());
            assertEquals(testProduct.getId(), theProductService.getProduct(num).getId());
        }

        int[] numsInvalid = {-1,-2,-3, 0};
        
        for (int num: numsInvalid){

            Mockito.when(theProductDAO.getProduct(num)).thenReturn(null);

            assertNull(theProductService.getProduct(num));
        }
        
        System.out.println("\nFinish test item: " + getClass().getName() + "\n");
    }

    @Test
    @DisplayName("Test for ProductService.getSortedProducts()")
    void getSortedProducts() {

        System.out.println("\nStart test item: " + getClass().getName() + "\n");

        int[] theSortField = {4, 5, 6, 7, 8, 1, 2, 0, 9, 999, -1, -999};
        String[] theSearchNameValid = {"Japan", "China", "Polar", "J", "C", "S", ""};
        String[] theSearchNameInValid = {"ZZ", "123", "A1", "f2qfawv"};

        for (int num: theSortField){
            
            for (String validS: theSearchNameValid){

                List<Product> testProducts = new ArrayList<>();
                testProducts.add(new Product());

                Mockito.when(theProductDAO.getSortedProducts(num, validS)).thenReturn(testProducts);

                assertNotEquals(0, theProductService.getSortedProducts(num, validS).size());
            }

            for (String invalidS: theSearchNameInValid){

                List<Product> testProducts = new ArrayList<>();

                Mockito.when(theProductDAO.getSortedProducts(num, invalidS)).thenReturn(testProducts);

                assertEquals(0, theProductDAO.getSortedProducts(num, invalidS).size());
            }
        }

        System.out.println("\nFinish test item: " + getClass().getName() + "\n");
    }
    
    @Test
    @DisplayName("Test for ProductService.saveProduct()")
    void saveProductTest() {

        System.out.println("\nStart test item: " + getClass().getName() + "\n");

        String[] theProductNames = {"NY", "LA", "Bora Bora"};

        for (String theProductName: theProductNames){

            Product theProduct = new Product();
            theProduct.setName(theProductName);

            ProductService theMockProductService = mock(ProductService.class);

            theMockProductService.saveProduct(theProduct);

            ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
            verify(theMockProductService).saveProduct(productCaptor.capture());
            
            Product actualProduct = productCaptor.getValue();

            assertEquals(theProduct, actualProduct);
        }

        System.out.println("\nFinish test item: " + getClass().getName() + "\n");
    }

    @Test
    @DisplayName("Test for ProductService.deleteProduct()")
    void deleteProductTest(){

        System.out.println("\nStart test item: " + getClass().getName() + "\n");

        int[] numValids = {1,2,3,7};

        for (int numValid: numValids){

            theProductService.deleteProduct(numValid);

            Mockito.when(theProductDAO.getProduct(numValid)).thenReturn(null);

            Product actualProduct = theProductService.getProduct(numValid);

            assertNull(actualProduct);   
        }

        System.out.println("\nFinish test item: " + getClass().getName() + "\n");
    }
}
