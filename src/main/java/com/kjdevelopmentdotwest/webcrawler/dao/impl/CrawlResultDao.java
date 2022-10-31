package com.kjdevelopmentdotwest.webcrawler.dao.impl;

import com.kjdevelopmentdotwest.webcrawler.dao.api.Dao;
import com.kjdevelopmentdotwest.webcrawler.dao.model.CrawlResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class CrawlResultDao implements Dao<CrawlResult> {

    @PersistenceContext
    private EntityManager entityManager;

    private final InnerCrawlResultDao innerCrawlResultDao;

    @Autowired
    public CrawlResultDao(InnerCrawlResultDao innerCrawlResultDao) {
        this.innerCrawlResultDao = innerCrawlResultDao;
    }

    /**
     * Returns crawl result with provided id
     * @return crawl result with provided id or null, if there is no crawl result with provided id
     */
    public CrawlResult findById(Integer id){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CrawlResult> criteriaQuery = criteriaBuilder.createQuery(CrawlResult.class);
        Root<CrawlResult> root = criteriaQuery.from(CrawlResult.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("id"), id));
        TypedQuery<CrawlResult> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList().stream().findFirst().orElse(null);
    }

    /**
     * Returns all crawl results persisted in db
     * @return list of all crawl results persisted in db, or empty list if db is empty
     */
    public List<CrawlResult> findAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CrawlResult> criteriaQuery = criteriaBuilder.createQuery(CrawlResult.class);

        criteriaQuery.from(CrawlResult.class);

        TypedQuery<CrawlResult> query = entityManager.createQuery(criteriaQuery);

        return query.getResultList();
    }

    @Override
    @Transactional
    public CrawlResult save(CrawlResult entity) {
        entity.setId(null);
        entityManager.persist(entity);
        entity.getInnerCrawlResults().forEach(innerCrawlResult -> {
            innerCrawlResult.setCrawlResultId(entity.getId());
            innerCrawlResultDao.save(innerCrawlResult);
        });
        entityManager.flush();
        return entity;
    }
}
