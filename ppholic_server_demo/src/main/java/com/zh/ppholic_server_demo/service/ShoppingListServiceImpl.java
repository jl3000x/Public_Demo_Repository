package com.zh.ppholic_server_demo.service;

import com.zh.ppholic_server_demo.dao.ShoppingListDAO;
import com.zh.ppholic_server_demo.entity.ShoppingList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShoppingListServiceImpl implements ShoppingListService {

    @Autowired
    private ShoppingListDAO theShoppingListDao;

    @Override
    @Transactional
    public ShoppingList getShoppingList(int theId) {
        
        return theShoppingListDao.getShoppingList(theId);
    }

    @Override
    @Transactional
    public void saveShoppingList(ShoppingList theShoppingList) {
        
        theShoppingListDao.saveShoppingList(theShoppingList);
    }

    @Override
    @Transactional
    public void deleteShoppingList(int theId) {
        
        theShoppingListDao.deleteShoppingList(theId);
    }
}
