package com.kjdevelopmentdotwest.webcrawler.service.csvgenerator;

import com.kjdevelopmentdotwest.webcrawler.dao.model.CrawlResult;
import com.kjdevelopmentdotwest.webcrawler.dao.model.InnerCrawlResult;
import com.kjdevelopmentdotwest.webcrawler.service.comparator.InnerCrawlResultReversedComparator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class provides methods for generating csv
 */
@Component
public class CsvGenerator {

    /**
     * Generates string representation of crawl result in csv format
     * @param crawlResult source crawl result
     * @return generated csv
     */
    public String generateCsv(CrawlResult crawlResult){
        return generateCsv(crawlResult, 0);
    }

    /**
     * Generates string representation of crawl result in csv format
     * @param crawlResult source crawl result
     * @param limit amount of top sites sorted by total hits. negative value if no need to sort and limit
     * @return string representation of crawl result in csv format
     */
    public String generateCsv(CrawlResult crawlResult, int limit){

        if (limit < 0 || limit > crawlResult.getInnerCrawlResults().size() - 1) {
            limit = 0;
        }

        StringBuilder builder = new StringBuilder();
        List<Integer> total = new ArrayList<>();

        builder.append("Seed:")
                .append("\n")
                .append(" ,")
                .append(crawlResult.getSeed())
                .append("\n");

        builder.append("Terms:")
                .append("\n")
                .append(" ,");

        crawlResult.getInnerCrawlResults().stream().findAny().get().getTermHits().forEach(term -> {
            builder.append(term.getName());
            builder.append(" ,");
            total.add(0);
        });

        builder.append("Total")
                .append("\n")
                .append("Output:")
                .append("\n");

        if (limit > 0){
            List<InnerCrawlResult> innerCrawlResults = crawlResult.getInnerCrawlResults().stream().sorted(new InnerCrawlResultReversedComparator()).toList();
            innerCrawlResults.stream().limit(limit).forEach(innerCrawlResult -> appendInnerCrawlResult(innerCrawlResult, builder, total));
        } else {
            crawlResult.getInnerCrawlResults().forEach(innerCrawlResult -> appendInnerCrawlResult(innerCrawlResult, builder, total));
        }

        builder.append("Total:")
                .append("\n");

        total.forEach(sum -> builder.append(" ,").append(sum));
        builder.append(" ,").append(total.stream().mapToInt(Integer::intValue).sum());
        return builder.toString();
    }

    private void appendInnerCrawlResult(InnerCrawlResult innerCrawlResult, StringBuilder target, List<Integer> total){
        target.append(innerCrawlResult.getUrl());
        AtomicInteger counter = new AtomicInteger(0);
        innerCrawlResult.getTermHits().forEach(term -> {
            int index = counter.getAndIncrement();
            total.set(index, total.get(index) + term.getHits());
            target.append(" ,")
                    .append(term.getHits());
        });
        target.append(" ,")
                .append(innerCrawlResult.getTotal())
                .append("\n");
    }

}
