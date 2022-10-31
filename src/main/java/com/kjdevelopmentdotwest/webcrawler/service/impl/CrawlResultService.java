package com.kjdevelopmentdotwest.webcrawler.service.impl;

import com.kjdevelopmentdotwest.webcrawler.dao.impl.CrawlResultDao;
import com.kjdevelopmentdotwest.webcrawler.dao.model.CrawlResult;
import com.kjdevelopmentdotwest.webcrawler.service.csvgenerator.CsvGenerator;
import com.kjdevelopmentdotwest.webcrawler.service.exception.WebCrawlerException;
import com.kjdevelopmentdotwest.webcrawler.service.webcrawler.WebCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * This class represents crawl result service
 */
@Service
public class CrawlResultService {

    private static final String LINK_REGEX = "https://(\\w+\\.)*(\\w+).*";
    private static final Pattern LINK_PATTERN = Pattern.compile(LINK_REGEX);

    private final WebCrawler webCrawler;
    private final CrawlResultDao dao;
    private final CsvGenerator csvGenerator;

    @Autowired
    public CrawlResultService(WebCrawler webCrawler, CrawlResultDao dao, CsvGenerator csvGenerator) {
        this.webCrawler = webCrawler;
        this.dao = dao;
        this.csvGenerator = csvGenerator;
    }

    /**
     * Generates crawl results and returns string representation of crawl result in csv format
     * @param seed initial url
     * @param limit max visited urls
     * @param depth in-depth traversals from the seed
     * @param top amount of top sites sorted by total hits. negative value if no need to sort and limit
     * @param termString terms to be searched fot, separated by comma
     * @return string representation of crawl result in csv format
     */
    public String getCsv(String seed, String limit, String depth, String top, String termString) {
        int limitInt;
        int depthInt;
        int topInt;
        List<String> terms = Arrays.stream(termString.split(",")).map(String::trim).filter(term -> !term.isEmpty()).collect(Collectors.toCollection(ArrayList::new));

        if (!LINK_PATTERN.matcher(seed).matches()){
            throw new WebCrawlerException("Corrupted seed");
        }

        try {
            limitInt = Integer.parseInt(limit);
            if (limitInt < 2){
                limitInt = 10000;
            }
        } catch (NumberFormatException ignored){
            limitInt = 10000;
        }

        try {
            depthInt = Integer.parseInt(depth);
            if (depthInt < 3){
                depthInt = 8;
            }
        } catch (NumberFormatException ignored){
            depthInt = 8;
        }

        try {
            topInt = Integer.parseInt(top);
            if (topInt < 0){
                topInt = 0;
            }
        } catch (NumberFormatException ignored){
            topInt = 0;
        }

        CrawlResult crawlResult = dao.save(webCrawler.crawlParallel(seed, depthInt, limitInt, terms));

        return csvGenerator.generateCsv(crawlResult, topInt);
    }

}
