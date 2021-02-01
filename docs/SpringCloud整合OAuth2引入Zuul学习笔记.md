1. 添加依赖
    ```xml
    <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
    </dependency>
    ```
2. 启动类添加@EnableZuulProxy注解
3. 添加认证过滤器
    ```java
    @Slf4j
    @Component
    public class OAuth2AuthenticationFilter extends ZuulFilter {
        @Autowired
        private RestTemplate restTemplate ;
        @Override
        public String filterType() {
            return "pre";
        }
        @Override
        public int filterOrder() {
            return 1;
        }
        @Override
        public boolean shouldFilter() {
            return true;
        }
        @Override
        public Object run() throws ZuulException {
            log.info("====> authentication filter start..");
            RequestContext currentContext = RequestContext.getCurrentContext();
            HttpServletRequest request = currentContext.getRequest();
            String authorization = request.getHeader("Authorization");
            if (StringUtils.isBlank(authorization)){
                return null ;
            }
            // 校验token的合法性
            if (!StringUtils.startsWith(authorization,"bearer ")){
                return null ;
            }
            TokenInfo tokenInfo = getTokenInfo(authorization);
            request.setAttribute("tokenInfo", tokenInfo);
            return null;
        }
        // 校验token的合法性
        private TokenInfo getTokenInfo(String authorization) {
            String url = "http://localhost:7777/oauth/check_token" ;
            String token = StringUtils.substringAfter(authorization, "bearer ") ;
            HttpHeaders headers = new HttpHeaders() ;
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set(HttpHeaders.AUTHORIZATION, "Basic " + CommonUtils.base64Encode("order_service:secret"));
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>() ;
            params.add("token", token);
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers) ;
            //String url, HttpMethod method, @Nullable HttpEntity<?> requestEntity,
            //			Class<T> responseType
            ResponseEntity<TokenInfo> exchange = restTemplate.exchange(url, HttpMethod.POST, httpEntity, TokenInfo.class);
            log.info("token info : {}", exchange.getBody());
            return exchange.getBody() ;
        }
    }
    ```
4. 添加授权过滤器
    ```text
    @Slf4j
    @Component
    public class OAuth2AuthorizationFilter extends ZuulFilter {
        @Override
        public String filterType() {
            return "pre";
        }
        @Override
        public int filterOrder() {
            return 3;
        }
        @Override
        public boolean shouldFilter() {
            return true;
        }
        @Override
        public Object run() throws ZuulException {
            log.info("====> authorization filter start..");
            RequestContext currentContext = RequestContext.getCurrentContext();
            HttpServletRequest request = currentContext.getRequest();
            TokenInfo tokenInfo = (TokenInfo) request.getAttribute("tokenInfo");
            if (tokenInfo != null && tokenInfo.isActive()){
                if (!hasPermission(tokenInfo, request)){
                    log.info("audit log update fail 403");
                    handleError(403, currentContext) ;
                }
                // 将用户信息放在header中传给后面的微服务
                String username = tokenInfo.getUser_name();
                currentContext.addZuulRequestHeader("username", username);
            }else {
                if (!isOauthServerRequest(request)){
                    log.info("audit log update fail 401");
                    handleError(401, currentContext) ;
                }
            }
            return null;
        }
        private boolean isOauthServerRequest(HttpServletRequest request){
            String uri = request.getRequestURI();
            return StringUtils.startsWith(uri, "/oauth") ;
        }
        // 是否有权限访问资源
        private boolean hasPermission(TokenInfo tokenInfo, HttpServletRequest request) {
            return RandomUtils.nextInt() %2 == 0 ;
        }
        private void handleError(int status, RequestContext ctx) {
            ctx.getResponse().setContentType("application/json");
            ctx.setResponseStatusCode(status);
            ctx.setResponseBody("{\"message\":\"audit fail !\"}");
            ctx.setSendZuulResponse(false);
        }
    }
    ```
5. Token信息entity编写
    ```text
    @Data
    public class TokenInfo {
        private boolean active;
        private String user_name ;
        private String client_id ;
        private Date exp ;
        private String [] scope ;
        private String [] authorities ;
        private String [] aud ;
    }
    ```
6. 删除资源服务器中oauth2相关权限校验

