package com.kjdevelopmentdotwest.webcrawler.service.webcrawler;

import com.kjdevelopmentdotwest.webcrawler.dao.model.CrawlResult;
import com.kjdevelopmentdotwest.webcrawler.dao.model.InnerCrawlResult;
import com.kjdevelopmentdotwest.webcrawler.dao.model.Term;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Queue;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

/**
 * This class provides method for generating crawl result
 */
@Component
public class WebCrawler {

    private static final String BREAK = "break";
    private static final String LINK_REGEX = "https://(\\w+\\.)*(\\w+)";
    private static final Pattern LINK_PATTERN = Pattern.compile(LINK_REGEX);
    private static final String LINK_TAG = "a[href]";
    private static final String LINK_ATTRIBUTE = "href";
    private static final String REGEX_WEB_EXTENDED = "/web/\\d*/.*";
    private static final String REGEX_WEB = "/web/\\d*/";
    private static final String EMPTY = "";

    /**
     * Crawls seed for terms in parallel
     * @param seed initial url
     * @param linkDepth in-depth traversals from the seed
     * @param limit max visited urls
     * @param terms terms to be searched fot
     * @return crawl result
     */
    public CrawlResult crawlParallel(String seed, int linkDepth, int limit, List<String> terms) {
        Queue<String> websitesToVisit = new ConcurrentLinkedQueue<>();
        Queue<String> visitedWebsites = new ConcurrentLinkedQueue<>();
        CrawlResult crawlResult = new CrawlResult(null, seed, new ArrayList<>());
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        AtomicInteger activeThreads = new AtomicInteger(0);
        int counter = 0;
        int layerCounter = 0;

        websitesToVisit.add(seed);
        websitesToVisit.add(BREAK);

        ExecutorService executorService = Executors.newCachedThreadPool();

        do {

            while (counter < limit && layerCounter < linkDepth){

                while (!websitesToVisit.isEmpty() && counter < limit && layerCounter < linkDepth){
                    String currentURL = websitesToVisit.remove();

                    if (!currentURL.equals(BREAK)){
                        visitedWebsites.add(currentURL);
                    } else {
                        while (activeThreads.get() > 0){
                            lock.lock();
                            try {
                                condition.await();
                            } catch (InterruptedException e) {
                                System.err.println("Thread: " + Thread.currentThread().getName() + " interrupted");
                                Thread.currentThread().interrupt();
                            }
                            lock.unlock();
                        }
                        websitesToVisit.add(BREAK);
                        layerCounter++;
                        continue;
                    }
                    counter++;
                    activeThreads.incrementAndGet();
                    executorService.execute(new CrawlUrlRunnable(lock, condition, currentURL, crawlResult, terms, websitesToVisit, visitedWebsites, activeThreads));
                }

                lock.lock();
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    System.err.println("Thread: " + Thread.currentThread().getName() + " interrupted");
                    Thread.currentThread().interrupt();
                }
                lock.unlock();
            }

            while (websitesToVisit.isEmpty() && activeThreads.get() > 0){
                lock.lock();
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    System.err.println("Thread: " + Thread.currentThread().getName() + " interrupted");
                    Thread.currentThread().interrupt();
                }
                lock.unlock();
            }

        } while (activeThreads.get() > 0);

        return crawlResult;
    }

    private class CrawlUrlRunnable implements Runnable {

        private final ReentrantLock lock;
        private final Condition condition;
        private final String url;
        private final CrawlResult target;
        private final List<String> terms;
        private final Queue<String> websitesToVisit;
        private final Queue<String> visitedWebsites;
        private final AtomicInteger activeThreads;

        private CrawlUrlRunnable(ReentrantLock lock,
                                 Condition condition,
                                 String url,
                                 CrawlResult target,
                                 List<String> terms,
                                 Queue<String> websitesToVisit,
                                 Queue<String> visitedWebsites,
                                 AtomicInteger activeThreads){
            this.lock = lock;
            this.condition = condition;
            this.url = url;
            this.target = target;
            this.terms = terms;
            this.websitesToVisit = websitesToVisit;
            this.visitedWebsites = visitedWebsites;
            this.activeThreads = activeThreads;
        }

        @Override
        public void run() {
            InnerCrawlResult innerCrawlResult = new InnerCrawlResult(null, url, null, 0, new ArrayList<>());

            terms.forEach(term -> innerCrawlResult.getTermHits().add(new Term(null, term, 0, null)));

            try {
                Document document = Jsoup.connect(url).get();
                String documentAsString = document.text();

                terms.forEach(termName -> {
                    int termHits = StringUtils.countMatches(documentAsString, termName);
                    Term term = innerCrawlResult.getTermHits()
                            .stream()
                            .filter(termString -> termString.getName().equals(termName))
                            .findAny()
                            .get();
                    term.setHits(termHits);
                });

                innerCrawlResult.setTotal(innerCrawlResult.getTermHits()
                        .stream()
                        .map(Term::getHits)
                        .mapToInt(Integer::intValue)
                        .sum());

                Elements links = document.select(LINK_TAG);
                links.stream()
                        .map(link -> link.attr(LINK_ATTRIBUTE))
                        .distinct()
                        .filter(link -> LINK_PATTERN.matcher(link).find())
                        .map(link -> {
                            if (link.matches(REGEX_WEB_EXTENDED)){
                                return link.replaceAll(REGEX_WEB, EMPTY);
                            } else {
                                return link;
                            }
                        })
                        .filter(link -> !visitedWebsites.contains(link))
                        .forEach(websitesToVisit::add);

                target.getInnerCrawlResults().add(innerCrawlResult);
            } catch (IOException | IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } finally {
                activeThreads.decrementAndGet();
                lock.lock();
                condition.signal();
                lock.unlock();
            }
        }
    }

}
