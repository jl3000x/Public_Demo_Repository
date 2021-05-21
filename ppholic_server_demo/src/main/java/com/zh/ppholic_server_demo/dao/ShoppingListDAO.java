package com.zh.ppholic_server_demo.dao;

import com.zh.ppholic_server_demo.entity.ShoppingList;

public interface ShoppingListDAO {
    
    ShoppingList getShoppingList(int theShoppingListId);

    void saveShoppingList(ShoppingList product);

    void deleteShoppingList(int theShoppingListId);
}
