package com.zh.ppholic_server_demo.service;

import java.util.List;

import com.zh.ppholic_server_demo.entity.SubCategory;

public interface SubCategoryService {
    
    SubCategory getSubCategory(int theSubCategoryId);

    List<SubCategory> getAllSubCategory();
}
