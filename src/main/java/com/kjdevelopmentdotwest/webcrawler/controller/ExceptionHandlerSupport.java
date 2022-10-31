package com.kjdevelopmentdotwest.webcrawler.controller;

import com.kjdevelopmentdotwest.webcrawler.service.exception.WebCrawlerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlerSupport {

    @ExceptionHandler
    public ResponseEntity<Object> handle(Exception exception){
        Map<String, Object> hashMap = new HashMap<>();
        if (exception instanceof WebCrawlerException){
            hashMap.put("message", exception.getMessage());
            return new ResponseEntity<>(hashMap, HttpStatus.BAD_REQUEST);
        } else {
            hashMap.put("message", "internal server error");
            return new ResponseEntity<>(hashMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
