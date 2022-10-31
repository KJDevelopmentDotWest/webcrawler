package com.kjdevelopmentdotwest.webcrawler.dao.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;
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
@Table(name = "crawl_result")
@AllArgsConstructor
@NoArgsConstructor
public class CrawlResult implements EntityModel{
    @Id
    @SequenceGenerator(name = "idSequenceCrawlResult", sequenceName = "crawl_result_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "idSequenceCrawlResult")
    private Integer id;

    private String seed;

    @OneToMany(orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "crawl_result_id")
    private List<InnerCrawlResult> innerCrawlResults;
}
