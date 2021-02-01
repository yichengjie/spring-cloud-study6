package com.yicj.oauth2.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {
    @PostMapping
    public PriceInfo create(@RequestBody OrderInfo info, @RequestHeader("username") String username){
        log.info("====> username : {}", username);
        //String url = "http://localhost:9060/prices/" + info.getProductId() ;
        //PriceInfo priceInfo = restTemplate.getForObject(url, PriceInfo.class);
        PriceInfo priceInfo = new PriceInfo() ;
        priceInfo.setId(info.getProductId());
        BigDecimal price = BigDecimal.valueOf(info.getProductId() *5) ;
        priceInfo.setPrice(price);
        log.info("price is : {}", priceInfo.getPrice());
        return priceInfo ;
    }
}
