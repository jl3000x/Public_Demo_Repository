package com.zh.ppholic_server_demo.service;

import java.util.List;

import com.zh.ppholic_server_demo.dao.ProductDAO;
import com.zh.ppholic_server_demo.entity.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDAO theProductDAO;

    @Override
    @Transactional
    public Product getProduct(int theId) {
        
        return theProductDAO.getProduct(theId);
    }

    @Override
    @Transactional
    public List<Product> getSortedProducts(int theSortField, String theSearchName) {
        
        return theProductDAO.getSortedProducts(theSortField, theSearchName);
    }

    @Override
    @Transactional
    public void saveProduct(Product theProduct) {
        
        theProductDAO.saveProduct(theProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(int theId) {
        
        theProductDAO.deleteProduct(theId);
    }
}
