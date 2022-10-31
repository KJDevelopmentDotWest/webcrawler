package com.kjdevelopmentdotwest.webcrawler.service.exception;

public class WebCrawlerException extends RuntimeException{
    public WebCrawlerException() {
        super();
    }

    public WebCrawlerException(String message) {
        super(message);
    }
}
