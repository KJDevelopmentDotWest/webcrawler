package com.kjdevelopmentdotwest.webcrawler.controller;

import com.kjdevelopmentdotwest.webcrawler.service.impl.CrawlResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/webcrawler")
public class Controller {

    private final CrawlResultService crawlResultService;

    @Autowired
    public Controller(CrawlResultService crawlResultService) {
        this.crawlResultService = crawlResultService;
    }

    @GetMapping("/csv")
    public ResponseEntity<byte[]> get(@RequestParam(value = "seed", defaultValue = "https://en.wikipedia.org/wiki/Elon_Musk") String seed,
                                      @RequestParam(value = "limit", defaultValue = "10000") String limit,
                                      @RequestParam(value = "depth", defaultValue = "8") String depth,
                                      @RequestParam(value = "top", defaultValue = "-1") String top,
                                      @RequestParam(value = "term", defaultValue = "Musk,Tesla") String term) throws UnsupportedEncodingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/csv");
        headers.add("Content-Disposition", "attachment;filename=result.csv");
        String csv = crawlResultService.getCsv(seed, limit, depth, top, term);
        return new ResponseEntity<>(csv.getBytes("ISO8859-15"), headers, HttpStatus.OK);
    }

}
