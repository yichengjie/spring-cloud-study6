package com.yicj.study.config;

import com.yicj.study.filter.AclInterceptor;
import com.yicj.study.filter.AuditLogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig implements WebMvcConfigurer {
    @Autowired
    private AuditLogInterceptor auditLogInterceptor ;
    @Autowired
    private AclInterceptor aclInterceptor ;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(auditLogInterceptor) ;
        registry.addInterceptor(aclInterceptor) ;
    }

}
