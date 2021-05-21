package com.zh.ppholic_server_demo.service;

import com.zh.ppholic_server_demo.model.ShoppingList;
import com.zh.ppholic_server_demo.model.User;

public interface ShoppingListService {
    
    ShoppingList getShoppingList(int theMemberId, String token);

    void saveShoppingList(User theUser, String token);
}