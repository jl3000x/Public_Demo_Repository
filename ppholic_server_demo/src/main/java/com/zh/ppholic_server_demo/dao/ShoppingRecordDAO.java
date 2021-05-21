package com.zh.ppholic_server_demo.dao;

import java.util.List;

import com.zh.ppholic_server_demo.entity.ShoppingRecord;

public interface ShoppingRecordDAO {
    
    ShoppingRecord getShoppingRecord(int theShoppingRecordId);

    ShoppingRecord getSortedShoppingRecord(int theSortField, int theId);
    
    List<ShoppingRecord> getSortedShoppingRecords (int theSortField, String theSearchName);

    void saveShoppingRecord(ShoppingRecord product);

    void deleteShoppingRecord(int theShoppingRecordId);
}
