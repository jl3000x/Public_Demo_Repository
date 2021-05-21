package com.zh.ppholic_server_demo.dao;

import java.util.List;

import com.zh.ppholic_server_demo.entity.Subtotal;

public interface SubtotalDao {

    Subtotal getSubtotal(int theSubtotalId);
    
    List<Subtotal> getSortedSubtotals (int theSortField, String theSearchName);

    void saveSubtotal(Subtotal product);

    void deleteSubtotal(int theSubtotalId);
}
