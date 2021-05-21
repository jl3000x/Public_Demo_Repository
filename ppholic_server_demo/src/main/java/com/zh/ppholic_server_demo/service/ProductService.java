package com.zh.ppholic_server_demo.service;

import java.util.List;

import com.zh.ppholic_server_demo.entity.Product;

public interface ProductService {
    
    Product getProduct(int theId);

    List<Product> getSortedProducts(int theSortField, String theSearchName);

    void saveProduct(Product theProduct);

    void deleteProduct(int theId);
}