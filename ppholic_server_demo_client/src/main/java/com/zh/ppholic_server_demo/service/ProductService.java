package com.zh.ppholic_server_demo.service;

import java.util.List;

import com.zh.ppholic_server_demo.model.Product;

public interface ProductService {
    
    Product getProduct(int theProductId);

    List<Product> getSortedProducts(int theSortField, String theSearchName);
}