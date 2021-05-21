package com.zh.ppholic_server_demo.service;

import java.util.List;

import com.zh.ppholic_server_demo.entity.User;
import com.zh.ppholic_server_demo.model.CrmUser;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    
    User getUser(String theSearchName);

    CrmUser getCrmUser(String theSearchName);

    List<User> getSortedUsers(int theSortField, String theSearchName);

    User getMember(String theSearchName);

    CrmUser getCrmMember(String theSearchName);

    List<User> getSortedMembers(int theSortField, String theSearchName);

    void saveUser(CrmUser theCrmUser);

    void deleteUser(String theSearchName);
}