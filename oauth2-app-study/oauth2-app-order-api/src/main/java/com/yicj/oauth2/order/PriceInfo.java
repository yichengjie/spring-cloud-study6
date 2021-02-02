package com.yicj.oauth2.order;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PriceInfo {
    private Integer id ;
    private BigDecimal price;
}
