package com.yicj.oauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@SpringBootApplication
public class ZuulServerSSOApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZuulServerSSOApplication.class, args) ;
    }
}
