package com.zh.ppholic_server_demo.service;

import com.zh.ppholic_server_demo.model.CrmUser;
import com.zh.ppholic_server_demo.model.User;

public interface UserService {

    public User getMember (String username, String token);

    CrmUser getCrmMember(String theSearchName, String token);
    
    String saveUser(CrmUser theCrmUser);

    void saveUser(CrmUser theCrmUser, String token);

    void deleteUser(String username, String token);
}