package com.zh.ppholic_server_demo.service;

import java.util.List;

import com.zh.ppholic_server_demo.dao.ShoppingRecordDAO;
import com.zh.ppholic_server_demo.entity.ShoppingRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShoppingRecordServiceImpl implements ShoppingRecordService {

    @Autowired
    private ShoppingRecordDAO theShoppingRecordDao;

    @Override
    @Transactional
    public ShoppingRecord getShoppingRecord(int theId) {
        
        return theShoppingRecordDao.getShoppingRecord(theId);
    }

    @Override
    @Transactional
    public ShoppingRecord getSortedShoppingRecord(int theSortField, int theId){

        return theShoppingRecordDao.getSortedShoppingRecord(theSortField, theId);
    }

    @Override
    @Transactional
    public List<ShoppingRecord> getSortedShoppingRecords(int theSortField, String theSearchName) {
        
        return theShoppingRecordDao.getSortedShoppingRecords(theSortField, theSearchName);
    }

    @Override
    @Transactional
    public void saveShoppingRecord(ShoppingRecord theShoppingRecord) {
        
        theShoppingRecordDao.saveShoppingRecord(theShoppingRecord);
    }

    @Override
    @Transactional
    public void deleteShoppingRecord(int theId) {
        
        theShoppingRecordDao.deleteShoppingRecord(theId);
    }
}
