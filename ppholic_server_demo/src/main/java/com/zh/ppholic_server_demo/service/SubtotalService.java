package com.zh.ppholic_server_demo.service;

import com.zh.ppholic_server_demo.entity.Subtotal;

public interface SubtotalService {
    
    Subtotal getSubtotal(int theSubtotalId);

    void saveSubtotal(Subtotal theSubtotal);

    void deleteSubtotal(int theSubtotalId);
}
