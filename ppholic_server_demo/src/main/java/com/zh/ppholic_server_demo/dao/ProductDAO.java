package com.zh.ppholic_server_demo.dao;

import java.util.List;

import com.zh.ppholic_server_demo.entity.Product;

public interface ProductDAO {
    
    Product getProduct(int theProductId);
    
    List<Product> getSortedProducts (int theSortField, String theSearchName);

    void saveProduct(Product product);

    void deleteProduct(int theProductId);
}