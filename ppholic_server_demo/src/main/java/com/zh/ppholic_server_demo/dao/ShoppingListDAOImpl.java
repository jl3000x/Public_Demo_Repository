package com.zh.ppholic_server_demo.dao;

import java.util.Date;

import javax.persistence.EntityManager;

import com.zh.ppholic_server_demo.entity.ShoppingList;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ShoppingListDAOImpl implements ShoppingListDAO{

    private EntityManager entityManager;
    
    // set up constructor injection
    @Autowired
    public ShoppingListDAOImpl(EntityManager theEntityManager) {

        entityManager = theEntityManager;
    }

    @Override
    public ShoppingList getShoppingList(int theShoppingListId) {
        
        Session currentSession = entityManager.unwrap(Session.class);

		// retrieve/read from database using product name
		Query<ShoppingList> theQuery = currentSession.createQuery("FROM ShoppingList WHERE id=:theShoppingListId", 
                                                                    ShoppingList.class);
		theQuery.setParameter("theShoppingListId", theShoppingListId);

		ShoppingList theShoppingList = null;
		
        // confirm the result is unique, or throw exception
        try {
			theShoppingList = theQuery.getSingleResult();
            currentSession.saveOrUpdate(theShoppingList);
		} catch (Exception e) {
		
            theShoppingList = null;
		}

		return theShoppingList;
    }

    @Override
    public void saveShoppingList(ShoppingList theShoppingList) {
        
        Session currentSession = entityManager.unwrap(Session.class);

        // update the last update time for the shopping list
        theShoppingList.setLastUpdate(new Date());
        
        currentSession.saveOrUpdate(theShoppingList);
    }

    @Override
    public void deleteShoppingList(int theShoppingListId) {
        
        Session currentSession = entityManager.unwrap(Session.class);
    
        Query<ShoppingList> theQuery = currentSession.createQuery("DELETE FROM ShoppingList WHERE id=:theShoppingListId");
        theQuery.setParameter("theShoppingListId", theShoppingListId);
        theQuery.executeUpdate();
    }
}
