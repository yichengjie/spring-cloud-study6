package com.yicj.oauth2.price;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PriceInfo {
    private Integer id ;
    private BigDecimal price;
}
