package com.yicj.oauth2.price;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@Slf4j
@RestController
@RequestMapping("/prices")
public class PriceController {

    @GetMapping("{id}")
    public PriceInfo getPrice(@PathVariable("id") Integer id){
        log.info("product id is : {}", id);
        PriceInfo priceInfo = new PriceInfo() ;
        priceInfo.setId(id);
        BigDecimal bigDecimal = BigDecimal.valueOf(id *5 ) ;
        priceInfo.setPrice(bigDecimal);
        return priceInfo ;
    }

}
