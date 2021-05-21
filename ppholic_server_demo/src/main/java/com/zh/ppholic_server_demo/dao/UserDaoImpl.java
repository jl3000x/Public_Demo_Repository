package com.zh.ppholic_server_demo.dao;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;

import com.zh.ppholic_server_demo.entity.Role;
import com.zh.ppholic_server_demo.entity.User;
import com.zh.ppholic_server_demo.util.SortUtils;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {

    // define field for entitymanager   
    private EntityManager entityManager;
    
    // set up constructor injection
    @Autowired
    public UserDaoImpl(EntityManager theEntityManager) {

        entityManager = theEntityManager;
    }

    @Override
	public User getUser(String theUserName) {

		// get the current hibernate session
        Session currentSession = entityManager.unwrap(Session.class);

		// retrieve/read from database using username
		Query<User> theQuery = currentSession.createQuery("FROM User WHERE userName=:theUserName", User.class);
		theQuery.setParameter("theUserName", theUserName);

		User theUser = null;
		
        // confirm the result is unique, or throw exception
        try {
			theUser = theQuery.getSingleResult();
		} catch (Exception e) {
			theUser = null;
		}

		return theUser;
	}

    @Override
    public List<User> getSortedUsers(int theSortField, String theSearchName) {

        // get the current hibernate session
        Session currentSession = entityManager.unwrap(Session.class);

        // determine sort field
		String theFieldName = null;
		
		switch (theSortField) {
            case SortUtils.USER_NAME:
				theFieldName = "username";
				break;
			case SortUtils.FIRST_NAME: 
				theFieldName = "first_name";
				break;
			case SortUtils.LAST_NAME:
				theFieldName = "last_name";
				break;
			case SortUtils.EMAIL:
				theFieldName = "email";
				break;
            case SortUtils.AMOUNT:
				theFieldName = "U.shoppingRecord.total DESC";
				break;
			default:
				// if nothing matches the default to sort by lastName
				theFieldName = "last_name";
		}
        
        Query<User> theQuery = null;

        if (theSearchName != null && theSearchName.trim().length() > 0) {
            // create a query and sorted by option
            String searchConfig = "WHERE lower(firstName) LIKE: keyword OR lower(lastName) LIKE :keyword ";
            String queryString = "FROM User U " + searchConfig + "ORDER BY " + theFieldName;
            theQuery = currentSession.createQuery(queryString, User.class);
            theQuery.setParameter("keyword", "%" + theSearchName.toLowerCase() + "%");
        }
        else {
            // collect all data and sorted by last name
            String queryString = "FROM User U ORDER BY " + theFieldName;
            theQuery = currentSession.createQuery(queryString, User.class);
        }

        // execute query and get list
        List<User> users = theQuery.getResultList();

        // return the result
        return users;
    }

    @Override
	public User getMember(String theUserName) {

		// get the current hibernate session
        Session currentSession = entityManager.unwrap(Session.class);

		// retrieve/read from database using username
		Query<User> theQuery = currentSession.createQuery("FROM User WHERE userName=:theUserName", User.class);
		theQuery.setParameter("theUserName", theUserName);

		User theUser = null;
		
        // confirm the result is unique, or throw exception
        try {
			theUser = theQuery.getSingleResult();

            // return only external member
            // due to incomplete db design, need extra O(1) time and space complexity
            LinkedList<Role> tempRoles = new LinkedList<>(theUser.getRoles());
            if (tempRoles.size() != 1 || tempRoles.get(0).getId() != 4) {
                
                theUser = null;
            }
		} catch (Exception e) {
			theUser = null;
		}

		return theUser;
	}

    @Override
    public List<User> getSortedMembers(int theSortField, String theSearchName) {

        // get the current hibernate session
        Session currentSession = entityManager.unwrap(Session.class);

        // determine sort field
		String theFieldName = null;
		
		switch (theSortField) {
            case SortUtils.USER_NAME:
				theFieldName = "username";
				break;
			case SortUtils.FIRST_NAME: 
				theFieldName = "first_name";
				break;
			case SortUtils.LAST_NAME:
				theFieldName = "last_name";
				break;
			case SortUtils.EMAIL:
				theFieldName = "email";
				break;
            case SortUtils.AMOUNT:
				theFieldName = "U.shoppingRecord.total DESC";
				break;
			default:
				// if nothing matches the default to sort by lastName
				theFieldName = "last_name";
		}
        
        Query<User> theQuery = null;

        if (theSearchName != null && theSearchName.trim().length() > 0) {
            // create a query and sorted by option
            String searchConfig = "WHERE lower(firstName) LIKE: keyword OR lower(lastName) LIKE :keyword ";
            String queryString = "FROM User U " + searchConfig + "ORDER BY " + theFieldName;
            theQuery = currentSession.createQuery(queryString, User.class);
            theQuery.setParameter("keyword", "%" + theSearchName.toLowerCase() + "%");
        }
        else {
            // collect all data and sorted by last name
            String queryString = "FROM User U ORDER BY " + theFieldName;
            theQuery = currentSession.createQuery(queryString, User.class);
        }

        // execute query and get list
        List<User> users = theQuery.getResultList();

        // return only external member
        // due to incomplete db design, need extra O(N) time and space complexity
        List<User> members = new ArrayList<>();

        for (User tempUser: users) {

            LinkedList<Role> tempRoles = new LinkedList<>(tempUser.getRoles());
            if (tempRoles.size() == 1 && tempRoles.get(0).getId() == 4){
                members.add(tempUser);
            }
        }

        // return the result
        return members;
    }

    @Override
    public void saveUser(User theUser) {

        // get the current hibernate session
        Session currentSession = entityManager.unwrap(Session.class);

        currentSession.clear();

        // create the user
		currentSession.saveOrUpdate(theUser);
    }

    @Override
    public void deleteUser(String userName) {

        // get the current hibernate session
        Session currentSession = entityManager.unwrap(Session.class);
    
        // save / update the user
        Query<User> theQuery = currentSession.createQuery("DELETE FROM User WHERE username=:userName");
        theQuery.setParameter("userName", userName);
        theQuery.executeUpdate();
    }
}
