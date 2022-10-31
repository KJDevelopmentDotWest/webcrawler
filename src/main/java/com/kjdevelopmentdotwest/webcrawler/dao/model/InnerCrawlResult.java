package com.kjdevelopmentdotwest.webcrawler.dao.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;
import java.util.List;

/**
 * Data class that represents inner crawl result
 */

@Data
@Entity
@Table(name = "inner_crawl_result")
@AllArgsConstructor
@NoArgsConstructor
public class InnerCrawlResult implements EntityModel {

    @Id
    @SequenceGenerator(name = "idSequenceInnerCrawlResult", sequenceName = "inner_crawl_result_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "idSequenceInnerCrawlResult")
    private Integer id;

    private String url;

    @Column(name = "crawl_result_id")
    private Integer crawlResultId;

    private Integer total;

    @OneToMany(orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "inner_crawl_result_id")
    private List<Term> termHits;
}
