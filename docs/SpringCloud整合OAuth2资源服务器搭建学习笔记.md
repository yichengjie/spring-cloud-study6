1. 添加依赖
    ```xml
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-oauth2</artifactId>
    </dependency>
    ```
2. 资源服务器的配置说明
    ```text
    2.1 配置资源服务器ID
    2.2 配置资源服务器如何验令牌
    ```
3. 资源服务器配置
    ```java
    @Configuration
    @EnableResourceServer
    public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {
        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources.resourceId("order-server") ;
        }
    }
    ```
4. 资源服务器验证令牌
    ```java
    @Configuration
    @EnableWebSecurity
    public class Oauth2WebSecurityConfig extends WebSecurityConfigurerAdapter {
        // 资源服务器的令牌服务// 如何验证令牌
        @Bean
        public ResourceServerTokenServices tokenServices(){
            RemoteTokenServices tokenServices = new RemoteTokenServices() ;
            tokenServices.setClientId("orderService");
            tokenServices.setClientSecret("123456");
            tokenServices.setCheckTokenEndpointUrl("http://localhost:9090/oauth/check_token");
            return tokenServices ;
        }
        // 认证跟用户相关信息就要配置AuthenticationManager
        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            OAuth2AuthenticationManager authenticationManager = new OAuth2AuthenticationManager() ;
            authenticationManager.setTokenServices(tokenServices());
            return authenticationManager;
        }
    }
    ```
5. 验证资源服务器功能
    ```text
    访问服务是否正常： http://localhost:9080/orders
    header参数 --> Authorization: bearer token
    ```