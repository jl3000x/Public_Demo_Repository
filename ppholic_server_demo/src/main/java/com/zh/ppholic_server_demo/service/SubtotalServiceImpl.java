package com.zh.ppholic_server_demo.service;

import com.zh.ppholic_server_demo.dao.SubtotalDao;
import com.zh.ppholic_server_demo.entity.Subtotal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubtotalServiceImpl implements SubtotalService {

    @Autowired
    private SubtotalDao theSubtotalDao;

    @Override
    @Transactional
    public Subtotal getSubtotal(int theSubtotalId) {
        
        return theSubtotalDao.getSubtotal(theSubtotalId);
    }

    @Override
    @Transactional
    public void saveSubtotal(Subtotal theSubtotal) {
        
        theSubtotalDao.saveSubtotal(theSubtotal);
    }

    @Override
    @Transactional
    public void deleteSubtotal(int theSubtotalId) {

        theSubtotalDao.deleteSubtotal(theSubtotalId);
    }
}
