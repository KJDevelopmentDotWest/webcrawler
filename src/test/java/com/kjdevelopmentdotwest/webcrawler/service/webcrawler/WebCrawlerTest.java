package com.kjdevelopmentdotwest.webcrawler.service.webcrawler;

import com.kjdevelopmentdotwest.webcrawler.dao.model.CrawlResult;
import com.kjdevelopmentdotwest.webcrawler.dao.model.InnerCrawlResult;
import com.kjdevelopmentdotwest.webcrawler.dao.model.Term;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class WebCrawlerTest {

    private InnerCrawlResult innerCrawlResult1 =
            new InnerCrawlResult(null, "https://example.org/", null, 2, List.of(new Term(null, "Example", 2, null)));
    private InnerCrawlResult innerCrawlResult2 =
            new InnerCrawlResult(null, "https://www.iana.org/domains/example", null, 1, List.of(new Term(null, "Example", 1, null)));
    private InnerCrawlResult innerCrawlResult3 =
            new InnerCrawlResult(null, "https://www.icann.org/privacy/policy", null, 0, List.of(new Term(null, "Example", 0, null)));
    private CrawlResult expectedCrawlResult = new CrawlResult(null, "https://example.org/", List.of(innerCrawlResult1, innerCrawlResult2, innerCrawlResult3));

    private WebCrawler crawler = new WebCrawler();

    @Test
    void webCrawlTest() {
        CrawlResult actualCrawlResult = crawler.crawlParallel("https://example.org/", 4, 3, List.of("Example"));
        Assertions.assertEquals(expectedCrawlResult, actualCrawlResult);
    }
}