package com.yicj.auth.core.config;

import com.yicj.auth.core.interceptor.RestTemplateUserContextInterceptor;
import com.yicj.auth.core.interceptor.UserContextInterceptor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@EnableWebMvc
@Configuration
public class CommonAutoConfiguration implements WebMvcConfigurer {

    // 请求拦截
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserContextInterceptor()) ;
    }

    // ResultTemplate拦截器，在发送请求前设置鉴权的用户上下文信息
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate() ;
        restTemplate.getInterceptors().add(new RestTemplateUserContextInterceptor());
        return restTemplate ;
    }
}
