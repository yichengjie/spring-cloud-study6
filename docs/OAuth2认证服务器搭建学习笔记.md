1. 添加依赖
    ```xml
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-oauth2</artifactId>
    </dependency>
    ```
2. 认证服务器的配置说明
    ```text
    2.1 配置客户端应用的详情信息（获取令牌、验令牌时使用）
    2.2 配置authenticationManager，让认证服务器能识别登录的用户
    2.3 配置验令牌需要的条件配置
    ```
3. 认证服务器配置类实现
    ```java
    @Configuration
    @EnableAuthorizationServer
    public class OAuth2AuthServerConfig extends AuthorizationServerConfigurerAdapter {
        @Autowired
        private AuthenticationManager authenticationManager ;
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
            ;
        }
        //2. 配置authenticationManager，让认证服务器能识别登录的用户
        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            // WebSecurityConfigurerAdapter 中配置AuthenticationManager
            // 1. 配置AuthenticationManagerBuilder
            // 2. 将AuthenticationManager暴露成spring容器中的bean
            endpoints.authenticationManager(authenticationManager) ;
        }
        //3. 配置验令牌需要的条件配置
        @Override
        public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
            // 验令牌的请求一定要经过身份认证
            security.checkTokenAccess("isAuthenticated()") ;
        }
    }
    ```
4. 配置AuthenticationManager发布到spring容器中供认证服务器识别用户使用
    ```java
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
    ```
5. 编写UserDetailsService实现类获取用户信息
    ```java
    @Service("userDetailServiceImpl")
    public class UserDetailServiceImpl implements UserDetailsService {
        @Autowired
        private PasswordEncoder passwordEncoder ;
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            // 模拟根据用户名，从数据库查询用户信息
            String password = passwordEncoder.encode("123456");
            return User.withUsername(username).password(password).authorities("ROLE_ADMIN").build();
        }
    }
    ```