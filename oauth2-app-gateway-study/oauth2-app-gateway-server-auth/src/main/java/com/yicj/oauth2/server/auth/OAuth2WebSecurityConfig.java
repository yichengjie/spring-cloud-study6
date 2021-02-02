package com.yicj.oauth2.server.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class OAuth2WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    @Qualifier("userDetailServiceImpl")
    private UserDetailsService userDetailsService ;
    @Autowired
    private PasswordEncoder passwordEncoder ;
    // WebSecurityConfigurerAdapter 中配置AuthenticationManager
    // >> 1. 配置AuthenticationManagerBuilder
    // 2. 将AuthenticationManager暴露成spring容器中的bean
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder) ;
    }
    // WebSecurityConfigurerAdapter 中配置AuthenticationManager
    // 1. 配置AuthenticationManagerBuilder
    // >> 2. 将AuthenticationManager暴露成spring容器中的bean
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
