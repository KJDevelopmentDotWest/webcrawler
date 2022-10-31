package com.kjdevelopmentdotwest.webcrawler.dao.impl;

import com.kjdevelopmentdotwest.webcrawler.dao.api.Dao;
import com.kjdevelopmentdotwest.webcrawler.dao.model.InnerCrawlResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Dao class fot inner crawl result entity
 */
@Repository
public class InnerCrawlResultDao implements Dao<InnerCrawlResult> {

    @PersistenceContext
    private EntityManager entityManager;

    private final TermDao termDao;

    @Autowired
    public InnerCrawlResultDao(TermDao termDao) {
        this.termDao = termDao;
    }

    @Override
    public InnerCrawlResult save(InnerCrawlResult entity) {
        entity.setId(null);
        entityManager.persist(entity);
        entity.getTermHits().forEach(term -> {
            term.setInnerCrawlResultId(entity.getId());
            termDao.save(term);
        });
        return entity;
    }
}
