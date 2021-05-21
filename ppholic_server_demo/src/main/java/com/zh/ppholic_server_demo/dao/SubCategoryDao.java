package com.zh.ppholic_server_demo.dao;

import java.util.List;

import com.zh.ppholic_server_demo.entity.SubCategory;

public interface SubCategoryDao {
    
    SubCategory getSubcategory(int theId);

    List<SubCategory> getAllSubCategory();
}
