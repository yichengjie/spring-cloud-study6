package com.yicj.auth.provider.controller;

import com.yicj.auth.core.utils.UserContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ProviderController {
    @GetMapping("/provider/test")
    public String test(HttpServletRequest request) {
        System.out.println("auth success, the user is : " + UserContextHolder.get().getUserName());
        System.out.println("----------------success access provider service----------------");
        return "success access provider service!";
    }
}
