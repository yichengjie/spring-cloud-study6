package com.yicj.auth.gateway.controller;

import com.yicj.auth.gateway.utils.JwtUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class TokenController {

    @GetMapping("/getToken/{name}")
    public Mono<String> get(@PathVariable("name") String name)  {
        return Mono.fromSupplier(()->JwtUtil.generateToken(name)) ;
    }
}
