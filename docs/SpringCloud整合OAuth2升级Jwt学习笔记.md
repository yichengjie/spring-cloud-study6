#### 认证服务器升级
1. 认证服务器添加jwtTokenStore配置
    ```java fragment
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
    ```
2. 认证服务器配置使用tokenStore和jwtTokenEnhancer
    ```java fragment
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
    ```
3. 认证服务器配置tokenKeyAccess的配置
    ```java fragment
    //3. 配置验令牌需要的条件配置
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // 验令牌的请求一定要经过身份认证
        security.checkTokenAccess("isAuthenticated()")
                .tokenKeyAccess("isAuthenticated()");
    }
    ```
#### Zuul网关升级
1. 添加依赖
    ```xml
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-oauth2</artifactId>
    </dependency>
    ```
2. 添加token校验相关配置(替代之前OAuth2AuthenticationFilter中的业务)
    ```properties
    # 网关启动的时候去认证服务器获取token_key
    security.oauth2.resource.jwt.key-uri=http://localhost:9090/oauth/token_key
    security.oauth2.client.client-id=gateway
    security.oauth2.client.client-secret=123456
    ```
3. 删除网关项目中自己实现的校验token的业务(jwt的token不需要去认证服务器上校验,直接将token传递到下游)
    ```text
    OAuth2AuthenticationFilter.java
    OAuth2AuthorizationFilter.java
    TokenInfo.java
    ```
4. 配置部分地址不需要拦截（发往认证服务器的请求）
    ```java
    @Configuration
    @EnableResourceServer
    public class GatewaySecurityConfig extends ResourceServerConfigurerAdapter {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/token/**").permitAll()
                    .anyRequest().authenticated();
        }
        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            // 默认resourceId为oauth2-resource，如果不配置会报错
            //access_denied: nvalid token does not contain resource id (oauth2-resource)
            resources.resourceId("order-server") ;
        }
    }
    ```
#### 下游REST服务升级
1. 添加依赖
    ```xml
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-oauth2</artifactId>
    </dependency>
    ```
2. 添加token校验相关配置
    ```properties
    # 应用启动的时候去认证服务器获取token_key
    security.oauth2.resource.jwt.key-uri=http://localhost:9090/oauth/token_key
    security.oauth2.client.client-id=orderService
    security.oauth2.client.client-secret=123456
    ```
3. 启动类上添加@EnableResourceServer注解
    ```java
    @EnableResourceServer
    @SpringBootApplication
    public class OrderApiApplication {
        public static void main(String[] args) {
            SpringApplication.run(OrderApiApplication.class, args) ;
        }
    }
    ```
4. 业务Controller中通过@AuthenticationPrincipal String principal注解获取用户信息
    ```java
    @RestController
    @RequestMapping("/orders")
    public class OrderController {
        @PostMapping
        public PriceInfo create(@RequestBody OrderInfo info, @AuthenticationPrincipal String principal){
            log.info("====> principal : {}", principal);
            //String url = "http://localhost:9060/prices/" + info.getProductId() ;
            //PriceInfo priceInfo = restTemplate.getForObject(url, PriceInfo.class);
            PriceInfo priceInfo = new PriceInfo() ;
            priceInfo.setId(info.getProductId());
            BigDecimal price = BigDecimal.valueOf(info.getProductId() *5) ;
            priceInfo.setPrice(price);
            log.info("price is : {}", priceInfo.getPrice());
            return priceInfo ;
        }
    }
    ```

