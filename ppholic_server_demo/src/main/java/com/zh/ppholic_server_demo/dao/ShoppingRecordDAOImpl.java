package com.zh.ppholic_server_demo.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import com.zh.ppholic_server_demo.entity.ShoppingRecord;
import com.zh.ppholic_server_demo.util.SortUtils;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ShoppingRecordDAOImpl implements ShoppingRecordDAO{

    private EntityManager entityManager;
    
    // set up constructor injection
    @Autowired
    public ShoppingRecordDAOImpl(EntityManager theEntityManager) {

        entityManager = theEntityManager;
    }

    @Override
    public ShoppingRecord getShoppingRecord(int theShoppingRecordId) {
        
        Session currentSession = entityManager.unwrap(Session.class);

		// retrieve/read from database using product name
		Query<ShoppingRecord> theQuery = currentSession.createQuery("FROM ShoppingRecord WHERE id=:theShoppingRecordId", 
                                                                    ShoppingRecord.class);
		theQuery.setParameter("theShoppingRecordId", theShoppingRecordId);

		ShoppingRecord theShoppingRecord = null;
		
        // confirm the result is unique, or throw exception
        try {
			theShoppingRecord = theQuery.getSingleResult();
            currentSession.saveOrUpdate(theShoppingRecord);
		} catch (Exception e) {

			theShoppingRecord = null;
		}

		return theShoppingRecord;
    }

    @Override
    public ShoppingRecord getSortedShoppingRecord(int theSortField, int theShoppingRecordId) {

        Session currentSession = entityManager.unwrap(Session.class);

		// retrieve/read from database using product name
		Query<ShoppingRecord> theQuery = currentSession.createQuery("FROM ShoppingRecord WHERE id=:theShoppingRecordId",
                                                                    ShoppingRecord.class);
		theQuery.setParameter("theShoppingRecordId", theShoppingRecordId);

		ShoppingRecord theShoppingRecord = null;
		
        // confirm the result is unique, or throw exception
        try {
			theShoppingRecord = theQuery.getSingleResult();
            currentSession.saveOrUpdate(theShoppingRecord);
		} catch (Exception e) {

			theShoppingRecord = null;
		}

		return theShoppingRecord;
    }

    @Override
    public List<ShoppingRecord> getSortedShoppingRecords(int theSortField, String theSearchName) {
        
        Session currentSession = entityManager.unwrap(Session.class);

        // determine sort field
        String theFieldName = null;
        
        switch (theSortField) {
            case SortUtils.PRODUCT_NAME:
                theFieldName = "S.product.name";
                break;
            case SortUtils.PRICE: 
                theFieldName = "S.product.price";
                break;
            case SortUtils.INFO:
                theFieldName = "S.product.information";
                break;
            case SortUtils.LAST_UPDATE:
                theFieldName = "S.product.last_update desc";
                break;
            case SortUtils.SUB_CATEGORY:
                theFieldName = "S.product.subCategory.name";
                break;
            default:
                // if nothing matches the default to sort by lastName
                theFieldName = "S.product.last_update desc";
        }
        
        Query<ShoppingRecord> theQuery = null;

        if (theSearchName != null && theSearchName.trim().length() > 0) {
            // create a query and sorted by option
            String searchConfig = "WHERE lower(S.product.name) LIKE: keyword OR lower(S.product.info) LIKE :keyword " +
                                    "OR lower(S.product.subCategory.name) LIKE :keyword ";
            String queryString = "FROM ShoppingRecord S " + searchConfig + "ORDER BY " + theFieldName;
            theQuery = currentSession.createQuery(queryString, ShoppingRecord.class);
            theQuery.setParameter("keyword", "%" + theSearchName.toLowerCase() + "%");
        }
        else {

            // collect all data and sorted by last name
            String queryString = "FROM ShoppingRecord S ORDER BY " + theFieldName;
            theQuery = currentSession.createQuery(queryString, ShoppingRecord.class);
        }

        // execute query and get list
        List<ShoppingRecord> subtotals = theQuery.getResultList();

        return subtotals;
    }

    @Override
    public void saveShoppingRecord(ShoppingRecord theShoppingRecord) {
        
        Session currentSession = entityManager.unwrap(Session.class);

        // update the last update time for the shopping list
        theShoppingRecord.setLastUpdate(new Date());
        
        currentSession.saveOrUpdate(theShoppingRecord);
    }

    @Override
    public void deleteShoppingRecord(int theShoppingRecordId) {
        
        Session currentSession = entityManager.unwrap(Session.class);
    
        Query<ShoppingRecord> theQuery = currentSession.createQuery("DELETE FROM ShoppingRecord WHERE id=:theShoppingRecordId");
        theQuery.setParameter("theShoppingRecordId", theShoppingRecordId);
        theQuery.executeUpdate();
    }
}
