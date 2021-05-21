package com.zh.ppholic_server_demo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import com.zh.ppholic_server_demo.entity.SubCategory;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SubCategoryDAOImpl implements SubCategoryDao {

    private EntityManager entityManager;

    // set up constructor injection
    @Autowired
    public SubCategoryDAOImpl(EntityManager theEntityManager) {

        entityManager = theEntityManager;
    }

    @Override
    public SubCategory getSubcategory(int theId) {

        Session currentSession = entityManager.unwrap(Session.class);

		// retrieve/read from database using username
		Query<SubCategory> theQuery = currentSession.createQuery("FROM SubCategory WHERE id=:theId", SubCategory.class);
        theQuery.setParameter("theId", theId);

		SubCategory theSubCategory = null;
		
        // confirm the result is unique, or throw exception
        try {
			theSubCategory = theQuery.getSingleResult();
		} catch (Exception e) {

			theSubCategory = null;
		}
        
        return theSubCategory;
    }

    @Override
    public List<SubCategory> getAllSubCategory() {

        Session currentSession = entityManager.unwrap(Session.class);

		// get all the SubCategory from DB
		Query<SubCategory> theQuery = currentSession.createQuery("FROM SubCategory", SubCategory.class);

        List<SubCategory> theSubCategories = theQuery.getResultList();

        return theSubCategories;
    }
}
