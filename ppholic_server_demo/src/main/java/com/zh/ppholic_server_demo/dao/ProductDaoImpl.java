package com.zh.ppholic_server_demo.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import com.zh.ppholic_server_demo.entity.Product;
import com.zh.ppholic_server_demo.util.SortUtils;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProductDAOImpl implements ProductDAO {
 
    private EntityManager entityManager;
    
    // set up constructor injection
    @Autowired
    public ProductDAOImpl(EntityManager theEntityManager) {

        entityManager = theEntityManager;
    }

    @Override
	public Product getProduct(int theProductId) {

        Session currentSession = entityManager.unwrap(Session.class);

		Query<Product> theQuery = currentSession.createQuery("FROM Product WHERE id=:theProductId", Product.class);
		theQuery.setParameter("theProductId", theProductId);

		Product theProduct = null;
		
        // confirm the result is unique, or throw exception
        try {
			theProduct = theQuery.getSingleResult();

            // sava or update the product with current time
            theProduct.setLastUpdate(new Date());
            currentSession.saveOrUpdate(theProduct);

		} catch (Exception e) {
			
            theProduct = null;
		}

		return theProduct;
	}

    @Override
    public List<Product> getSortedProducts(int theSortField, String theSearchName) {

        Session currentSession = entityManager.unwrap(Session.class);

		String theFieldName = null;
		
		switch (theSortField) {
            case SortUtils.PRODUCT_NAME:
				theFieldName = "name";
				break;
			case SortUtils.PRICE: 
				theFieldName = "price";
				break;
			case SortUtils.INFO:
				theFieldName = "information";
				break;
            case SortUtils.LAST_UPDATE:
				theFieldName = "last_update desc";
				break;
            case SortUtils.SUB_CATEGORY:
				theFieldName = "P.subCategory.name";
				break;
			default:
				// if nothing matches the default to sort by lastName
				theFieldName = "last_update desc";
		}
        
        Query<Product> theQuery = null;

        if (theSearchName != null && theSearchName.trim().length() > 0) {

            // create a query and sorted by option
            String searchConfig = "WHERE lower(name) LIKE: keyword OR lower(info) LIKE :keyword " +
                                    "OR lower(P.subCategory.name) LIKE :keyword ";
            String queryString = "FROM Product P " + searchConfig + "ORDER BY " + theFieldName;
            theQuery = currentSession.createQuery(queryString, Product.class);
            theQuery.setParameter("keyword", "%" + theSearchName.toLowerCase() + "%");
        }
        else {

            // collect all data and sorted by last name
            String queryString = "FROM Product P ORDER BY " + theFieldName;
            theQuery = currentSession.createQuery(queryString, Product.class);
        }

        // execute query and get list
        List<Product> products = theQuery.getResultList();

        return products;
    }

    
    @Override
    public void saveProduct(Product theProduct) {

        Session currentSession = entityManager.unwrap(Session.class);

        // sava or update the product with current time
        theProduct.setLastUpdate(new Date());

		currentSession.saveOrUpdate(theProduct);
    }

    @Override
    public void deleteProduct(int theProductId) {

        Session currentSession = entityManager.unwrap(Session.class);
        
        Query<Product> theQuery = currentSession.createQuery("DELETE FROM Product WHERE id=:theProductId");
        theQuery.setParameter("theProductId", theProductId);
        theQuery.executeUpdate();
    }
}
