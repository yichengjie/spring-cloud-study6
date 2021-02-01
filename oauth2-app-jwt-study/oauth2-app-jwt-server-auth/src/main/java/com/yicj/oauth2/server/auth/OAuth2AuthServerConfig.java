package com.yicj.oauth2.server.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private AuthenticationManager authenticationManager ;
    @Bean
    public TokenStore tokenStore (){
        //return new JdbcTokenStore(dataSource) ;
        return new JwtTokenStore(jwtTokenEnhancer()) ;
    }
    // 注意这里必须是public而且是@Bean否则资源服务会报404
    // 这里如果不申请为spring容器管理的bean则TokenEndpoint是不会暴漏/oauth/token端点
    @Bean
    public JwtAccessTokenConverter jwtTokenEnhancer() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter() ;
        converter.setSigningKey("123456");
        return converter ;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder() ;
    }

    //1.  配置客户端应用的详情信息（获取令牌、验令牌时使用）
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("orderApp")
                .secret(passwordEncoder().encode("123456"))
                .scopes("read","write")
                .accessTokenValiditySeconds(3600)
                .resourceIds("order-server")
                .authorizedGrantTypes("password")
                .and()
                .withClient("orderService")
                .secret(passwordEncoder().encode("123456"))
                .scopes("read")
                .accessTokenValiditySeconds(3600)
                .resourceIds("order-server")
                .authorizedGrantTypes("password")
                .and()
                .withClient("gateway")
                .secret(passwordEncoder().encode("123456"))
                .scopes("read")
                .accessTokenValiditySeconds(3600)
                .authorizedGrantTypes("password")
        ;
    }
    //2. 配置authenticationManager，让认证服务器能识别登录的用户
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // WebSecurityConfigurerAdapter 中配置AuthenticationManager
        // 1. 配置AuthenticationManagerBuilder
        // 2. 将AuthenticationManager暴露成spring容器中的bean
        endpoints.authenticationManager(authenticationManager)
        .tokenStore(tokenStore())// 配置token存放位置，默认存放在内存中
        .tokenEnhancer(jwtTokenEnhancer())
        ;
    }
    //3. 配置验令牌需要的条件配置
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // 验令牌的请求一定要经过身份认证
        security.checkTokenAccess("isAuthenticated()")
                .tokenKeyAccess("isAuthenticated()");
    }
}
