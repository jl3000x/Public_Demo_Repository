package com.zh.ppholic_server_demo.service;

import com.zh.ppholic_server_demo.entity.ShoppingList;

public interface ShoppingListService {
    
    ShoppingList getShoppingList(int theId);

    void saveShoppingList(ShoppingList theShoppingList);

    void deleteShoppingList(int theId);
}