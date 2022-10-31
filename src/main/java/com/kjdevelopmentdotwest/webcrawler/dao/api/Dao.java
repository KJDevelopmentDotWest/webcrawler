package com.kjdevelopmentdotwest.webcrawler.dao.api;

import com.kjdevelopmentdotwest.webcrawler.dao.model.EntityModel;

/**
 * Interface for dao classes
 * @param <T> type of entity this dao will operate
 */
public interface Dao<T extends EntityModel> {

    /**
     * Saves entity
     * @param entity entity to be saved
     * @return saved entity
     */
    T save(T entity);
}
