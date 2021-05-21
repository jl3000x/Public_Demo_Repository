package com.zh.ppholic_server_demo.dao;

import java.util.List;

import com.zh.ppholic_server_demo.entity.User;

public interface UserDao {
    
    User getUser(String userName);
    
    List<User> getSortedUsers (int theSortField, String theSearchName);

    User getMember(String theUserName);

    List<User> getSortedMembers(int theSortField, String theSearchName);

    void saveUser(User user);

    void deleteUser(String name);
}
