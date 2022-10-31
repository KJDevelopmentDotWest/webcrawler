package com.kjdevelopmentdotwest.webcrawler.dao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Configuration
@EnableTransactionManagement
public class SpringConfiguration {

    @Bean
    public PersistenceAnnotationBeanPostProcessor persistenceAnnotationBeanPostProcessor (){
        return new PersistenceAnnotationBeanPostProcessor();
    }

    @Bean
    public EntityManagerFactory entityManagerFactory (){
        return Persistence.createEntityManagerFactory("WebCrawler");
    }


    @Bean
    public TransactionManager transactionManager (){
        return new JpaTransactionManager();
    }

}
