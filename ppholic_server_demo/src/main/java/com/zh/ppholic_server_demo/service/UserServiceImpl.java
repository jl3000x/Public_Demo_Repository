package com.zh.ppholic_server_demo.service;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.zh.ppholic_server_demo.dao.ShoppingListDAO;
import com.zh.ppholic_server_demo.dao.ShoppingRecordDAO;
import com.zh.ppholic_server_demo.dao.UserDao;
import com.zh.ppholic_server_demo.entity.Role;
import com.zh.ppholic_server_demo.entity.ShoppingList;
import com.zh.ppholic_server_demo.entity.ShoppingRecord;
import com.zh.ppholic_server_demo.entity.User;
import com.zh.ppholic_server_demo.model.CrmRole;
import com.zh.ppholic_server_demo.model.CrmUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDao theUserDAO;

    @Autowired
    private ShoppingListDAO theShoppingListDAO;

    @Autowired
    private ShoppingRecordDAO theShoppingRecordDAO;
    
    @Autowired
    private BCryptPasswordEncoder theBCryptPasswordEncoder;

    @Override
    @Transactional
    public User getUser(String theSearchName) {

        return theUserDAO.getUser(theSearchName);
    }

    @Override
    @Transactional
    public CrmUser getCrmUser(String theSearchName){
        
        // create the new CRM user
        CrmUser theCrmUser = new CrmUser();

        // acquire the user information for the new crm user
        User theUser = theUserDAO.getUser(theSearchName);
        
        theCrmUser.setId(theUser.getId());
        theCrmUser.setUserName(theUser.getUserName());
        theCrmUser.setPassword(theBCryptPasswordEncoder.encode(theUser.getPassword()));
        theCrmUser.setMatchingPassword(theBCryptPasswordEncoder.encode(theUser.getPassword()));
        theCrmUser.setFirstName(theUser.getFirstName());
        theCrmUser.setLastName(theUser.getLastName());
        theCrmUser.setEmail(theUser.getEmail());
        theCrmUser.setShoppingList(theUser.getShoppingList());
        theCrmUser.setShoppingRecord(theUser.getShoppingRecord());
        theCrmUser.setCrmRole(new CrmRole(theUser.getRoles()));

        return theCrmUser;
    }

    @Override
    @Transactional
    public List<User> getSortedUsers(int theSortField, String theSearchName) {

        return theUserDAO.getSortedUsers(theSortField, theSearchName);
    }

    @Override
    @Transactional
    public User getMember(String theSearchName) {

        return theUserDAO.getMember(theSearchName);
    }

    @Override
    @Transactional
    public CrmUser getCrmMember(String theSearchName){
        
        // create the new CRM user
        CrmUser theCrmUser = new CrmUser();

        // acquire the user information for the new crm user
        User theUser = theUserDAO.getMember(theSearchName);
        
        theCrmUser.setId(theUser.getId());
        theCrmUser.setUserName(theUser.getUserName());
        theCrmUser.setPassword(theBCryptPasswordEncoder.encode(theUser.getPassword()));
        theCrmUser.setMatchingPassword(theBCryptPasswordEncoder.encode(theUser.getPassword()));
        theCrmUser.setFirstName(theUser.getFirstName());
        theCrmUser.setLastName(theUser.getLastName());
        theCrmUser.setEmail(theUser.getEmail());
        theCrmUser.setShoppingList(theUser.getShoppingList());
        theCrmUser.setShoppingRecord(theUser.getShoppingRecord());
        theCrmUser.setCrmRole(new CrmRole(theUser.getRoles()));
        return theCrmUser;
    }

    @Override
    @Transactional
    public List<User> getSortedMembers(int theSortField, String theSearchName) {

        return theUserDAO.getSortedMembers(theSortField, theSearchName);
    }

    @Override
    @Transactional
    public void saveUser(CrmUser theCrmUser) {
        
        // create the new user
        User theUser = new User();
        
        // assign details to the user object
        theUser.setId(theCrmUser.getId());
        theUser.setUserName(theCrmUser.getUserName());
        theUser.setPassword(theBCryptPasswordEncoder.encode(theCrmUser.getPassword()));
        theUser.setFirstName(theCrmUser.getFirstName());
        theUser.setLastName(theCrmUser.getLastName());
        theUser.setEmail(theCrmUser.getEmail());
        try {
            theUser.setShoppingList(getUser(theUser.getUserName()).getShoppingList());
            theUser.setShoppingRecord(getUser(theUser.getUserName()).getShoppingRecord());    
        } catch (Exception e) {
            ShoppingList theShoppingList = new ShoppingList();
            theShoppingListDAO.saveShoppingList(theShoppingList);

            ShoppingRecord theShoppingRecord = new ShoppingRecord();
            theShoppingRecordDAO.saveShoppingRecord(theShoppingRecord);

            theUser.setShoppingList(theShoppingList);
            theUser.setShoppingRecord(theShoppingRecord);
        }

        // define the role of the account
        List<Role> newRoles = new LinkedList<>();

        if (theCrmUser.getCrmRole().getIsEmployee()) {
            newRoles.add(new Role( 1, "ROLE_EMPLOYEE"));
        }

        if (theCrmUser.getCrmRole().getIsManager()) {
            newRoles.add(new Role( 2, "ROLE_MANAGER"));
        }

        if (theCrmUser.getCrmRole().getIsAdmin()) {
            newRoles.add(new Role( 3, "ROLE_ADMIN"));
        }

        if (theCrmUser.getCrmRole().getIsMember()) {
            newRoles.add(new Role( 4, "ROLE_MEMBER"));
        }

        theUser.setRoles(newRoles);

        // save user in the database
        theUserDAO.saveUser(theUser);
    }

    @Override
    @Transactional
    public void deleteUser(String theSearchName) {

        theUserDAO.deleteUser(theSearchName);
    }

    // override UserDetailsService "loadUserByUsername" method to provide extra user detail for Spring security.
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        User theUser = theUserDAO.getUser(userName);
        
        if (theUser == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        else { 
            return new org.springframework.security.core.userdetails.User(theUser.getUserName(),
                                                                            theUser.getPassword(),
                                                                            mapRolesToAuthorities(theUser.getRoles()));
        }
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {

        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}