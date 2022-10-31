package com.kjdevelopmentdotwest.webcrawler.service.csvgenerator;

import com.kjdevelopmentdotwest.webcrawler.dao.model.CrawlResult;
import com.kjdevelopmentdotwest.webcrawler.dao.model.InnerCrawlResult;
import com.kjdevelopmentdotwest.webcrawler.dao.model.Term;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvGeneratorTest {

    private CsvGenerator generator = new CsvGenerator();

    private InnerCrawlResult innerCrawlResult1 =
            new InnerCrawlResult(null, "https://example.org/", null, 2, List.of(new Term(null, "Example", 2, null)));
    private InnerCrawlResult innerCrawlResult2 =
            new InnerCrawlResult(null, "https://www.iana.org/domains/example", null, 1, List.of(new Term(null, "Example", 1, null)));
    private InnerCrawlResult innerCrawlResult3 =
            new InnerCrawlResult(null, "https://www.icann.org/privacy/policy", null, 0, List.of(new Term(null, "Example", 0, null)));
    private CrawlResult crawlResult = new CrawlResult(null, "https://example.org/", List.of(innerCrawlResult1, innerCrawlResult2, innerCrawlResult3));

    private String expectedCsv = """
            Seed:
             ,https://example.org/
            Terms:
             ,Example ,Total
            Output:
            https://example.org/ ,2 ,2
            https://www.iana.org/domains/example ,1 ,1
            https://www.icann.org/privacy/policy ,0 ,0
            Total:
             ,3 ,3""";

    @Test
    void generateCsvTest() {
        String actualCsv = generator.generateCsv(crawlResult, 10);
        Assertions.assertEquals(expectedCsv, actualCsv);
    }
}