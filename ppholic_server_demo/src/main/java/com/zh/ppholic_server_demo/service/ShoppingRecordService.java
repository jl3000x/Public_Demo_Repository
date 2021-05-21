package com.zh.ppholic_server_demo.service;

import java.util.List;

import com.zh.ppholic_server_demo.entity.ShoppingRecord;

public interface ShoppingRecordService {
    
    ShoppingRecord getShoppingRecord(int theId);

    ShoppingRecord getSortedShoppingRecord(int theSortField, int theId);

    List<ShoppingRecord> getSortedShoppingRecords(int theSortField, String theSearchName);

    void saveShoppingRecord(ShoppingRecord theShoppingRecord);

    void deleteShoppingRecord(int theId);
}