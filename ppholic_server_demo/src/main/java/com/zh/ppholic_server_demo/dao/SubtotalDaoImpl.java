package com.zh.ppholic_server_demo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import com.zh.ppholic_server_demo.entity.Subtotal;
import com.zh.ppholic_server_demo.util.SortUtils;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SubtotalDaoImpl implements SubtotalDao {

    // define field for entitymanager   
    private EntityManager entityManager;

    // set up constructor injection
    @Autowired
    public SubtotalDaoImpl(EntityManager theEntityManager) {

        entityManager = theEntityManager;
    }

    @Override
    public Subtotal getSubtotal(int theSubtotalId) {
        
        // get the current hibernate session
        Session currentSession = entityManager.unwrap(Session.class);

        // retrieve/read from database using product name
        Query<Subtotal> theQuery = currentSession.createQuery("FROM Subtotal WHERE id=:theSubtotalId", Subtotal.class);
        theQuery.setParameter("theSubtotalId", theSubtotalId);

        Subtotal theSubtotal = null;
        
        // confirm the result is unique, or throw exception
        try {
            theSubtotal = theQuery.getSingleResult();

            currentSession.saveOrUpdate(theSubtotal);
        } catch (Exception e) {
            theSubtotal = null;
        }

        return theSubtotal;
    }

    @Override
    public List<Subtotal> getSortedSubtotals(int theSortField, String theSearchName) {
        
        // get the current hibernate session
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
        
        Query<Subtotal> theQuery = null;

        if (theSearchName != null && theSearchName.trim().length() > 0) {
            // create a query and sorted by option
            String searchConfig = "WHERE lower(S.product.name) LIKE: keyword OR lower(S.product.info) LIKE :keyword " +
                                    "OR lower(S.product.subCategory.name) LIKE :keyword ";
            String queryString = "FROM Subtotal S " + searchConfig + "ORDER BY " + theFieldName;
            theQuery = currentSession.createQuery(queryString, Subtotal.class);
            theQuery.setParameter("keyword", "%" + theSearchName.toLowerCase() + "%");
        }
        else {
            // collect all data and sorted by last name
            String queryString = "FROM Subtotal S ORDER BY " + theFieldName;
            theQuery = currentSession.createQuery(queryString, Subtotal.class);
        }

        // execute query and get list
        List<Subtotal> subtotals = theQuery.getResultList();

        // return the result
        return subtotals;
    }

    @Override
    public void saveSubtotal(Subtotal theSubtotal) {
        
        // get the current hibernate session
        Session currentSession = entityManager.unwrap(Session.class);
        
        currentSession.saveOrUpdate(theSubtotal);
    }

    @Override
    public void deleteSubtotal(int theSubtotalId) {
        
        // get the current hibernate session
        Session currentSession = entityManager.unwrap(Session.class);
    
        // save / update the product
        Query<Subtotal> theQuery = currentSession.createQuery("DELETE FROM Subtotal WHERE id=:theSubtotalId");
        theQuery.setParameter("theSubtotalId", theSubtotalId);
        theQuery.executeUpdate();
    }
}
