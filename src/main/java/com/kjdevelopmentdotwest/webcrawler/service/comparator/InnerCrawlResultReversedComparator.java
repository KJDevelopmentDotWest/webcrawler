package com.kjdevelopmentdotwest.webcrawler.service.comparator;

import com.kjdevelopmentdotwest.webcrawler.dao.model.InnerCrawlResult;
import org.springframework.stereotype.Component;

import java.util.Comparator;

/**
 * InnerCrawlResultReversedComparator compares two inner crawl result by total hits
 */
@Component
public class InnerCrawlResultReversedComparator implements Comparator<InnerCrawlResult> {

    /**
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the first total hits is greater than, equal to, or less than the second.
     */
    @Override
    public int compare(InnerCrawlResult o1, InnerCrawlResult o2) {
        return o2.getTotal() - o1.getTotal();
    }
}
