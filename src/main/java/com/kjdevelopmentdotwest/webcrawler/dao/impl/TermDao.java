package com.kjdevelopmentdotwest.webcrawler.dao.impl;

import com.kjdevelopmentdotwest.webcrawler.dao.api.Dao;
import com.kjdevelopmentdotwest.webcrawler.dao.model.Term;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Dao class fot term entity
 */
@Repository
public class TermDao implements Dao<Term> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Term save(Term entity) {
        entity.setId(null);
        entityManager.persist(entity);
        return entity;
    }
}
