package com.yicj.study.component;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

//@RestControllerAdvice
public class ErrorHandler {
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Map<String, Object> handle(Exception e){
        Map<String, Object> info = new HashMap<>() ;
        info.put("msg", e.getMessage()) ;
        info.put("time", LocalDateTime.now()) ;
        return info ;
    }
}
