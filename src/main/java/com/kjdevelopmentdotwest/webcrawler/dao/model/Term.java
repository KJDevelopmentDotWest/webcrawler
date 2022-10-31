package com.kjdevelopmentdotwest.webcrawler.dao.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;

/**
 * Data class that represents term
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Term implements EntityModel{
    @Id
    @SequenceGenerator(name = "idSequenceTerm", sequenceName = "term_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "idSequenceTerm")
    private Integer id;

    private String name;

    private Integer hits;

    @Column(name = "inner_crawl_result_id")
    private Integer innerCrawlResultId;
}
