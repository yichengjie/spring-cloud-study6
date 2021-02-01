package com.yicj.oauth2.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private RestTemplate restTemplate ;


    @PostMapping
    public PriceInfo create(@RequestBody OrderInfo info){
        String url = "http://localhost:9060/prices/" + info.getProductId() ;
        PriceInfo priceInfo = restTemplate.getForObject(url, PriceInfo.class);
        log.info("price is : {}", priceInfo.getPrice());
        return priceInfo ;
    }
}
