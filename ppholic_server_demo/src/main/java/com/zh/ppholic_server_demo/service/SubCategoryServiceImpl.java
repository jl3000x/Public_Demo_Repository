package com.zh.ppholic_server_demo.service;

import java.util.List;

import com.zh.ppholic_server_demo.dao.SubCategoryDao;
import com.zh.ppholic_server_demo.entity.SubCategory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubCategoryServiceImpl implements SubCategoryService {

    @Autowired
    private SubCategoryDao theSubCategoryDao;

    @Override
    @Transactional
    public SubCategory getSubCategory(int theSubCategoryId) {
        
        return theSubCategoryDao.getSubcategory(theSubCategoryId);
    }

    @Override
    @Transactional
    public List<SubCategory> getAllSubCategory() {
        
        return theSubCategoryDao.getAllSubCategory();
    }
}
