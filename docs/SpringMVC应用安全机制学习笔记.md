#### 基于basic的安全认证
1. Basic认证
    ```java
    @Component
    public class BasicAuthenticationFilter extends OncePerRequestFilter {
        @Autowired
        private UserRepository repository ;
        @Override
        protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
            String authHeader = request.getHeader("Authorization");
            if (StringUtils.isNoneBlank(authHeader)){
                String token64 = StringUtils.substringAfter(authHeader, "Basic ");
                String token = new String(Base64Utils.decodeFromString(token64)) ;
                String [] items = StringUtils.splitByWholeSeparatorPreserveAllTokens(token, ":") ;
                String username = items[0] ;
                String password = items[1] ;
                User user = repository.findByUsername(username);
                if (user != null && StringUtils.equals(password, user.getPassword())){
                    request.setAttribute("user", user);
                }
            }
            chain.doFilter(request, response);
        }
    }
    ```
#### 基于Acl的授权模式
1. Acl授权核心代码编写
    ```java
    @Component
    public class AclInterceptor implements HandlerInterceptor {
        private String [] permitUrls = {"/users/login"} ;
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            boolean result = true ;
            // 如果在不需要权限认证集合中
            if (ArrayUtils.contains(permitUrls, request.getRequestURI())){
               return result ;
            }
            User user = (User) request.getAttribute("user");
            if (user == null){
                response.setContentType("text/plain");
                response.getWriter().write("need authentication");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().flush();
                result = false ;
            }else {
                String method = request.getMethod();
                if (!user.hasPermission(method)){
                    response.setContentType("text/plain");
                    response.getWriter().write("request forbidden");
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.getWriter().flush();
                    result = false ;
                }
            }
            return result;
        }
    }
    ```
2. 配置Acl拦截器
    ```java
    @Configuration
    public class SecurityConfig implements WebMvcConfigurer {
        @Autowired
        private AclInterceptor aclInterceptor ;
    
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(aclInterceptor) ;
        }
    }
    ```
#### 审计日志模块
1. 编写审计核心代码
    ```java
    @Component
    public class AuditLogInterceptor implements HandlerInterceptor {
    
        @Autowired
        private AuditLogRepository repository ;
    
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            AuditLog log = new AuditLog() ;
            log.setMethod(request.getMethod());
            log.setPath(request.getRequestURI());
            User user = (User)request.getAttribute("user");
            if (user != null){
                log.setUsername(user.getUsername());
            }
            log.setCreatedTime(LocalDateTime.now());
            log.setModifyTime(LocalDateTime.now());
            repository.save(log) ;
            request.setAttribute("auditLogId", log.getId());
            return true;
        }
    
        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
            Integer auditLogId = (Integer)request.getAttribute("auditLogId");
            AuditLog auditLog = repository.findById(auditLogId).get();
            auditLog.setStatus(response.getStatus());
            auditLog.setModifyTime(LocalDateTime.now());
            repository.save(auditLog) ;
        }
    }
    ```
2. 添加统一异常处理（默认出错后跳转到/error地址）
    ```java
    @RestControllerAdvice
    public class ErrorHandler {
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        @ExceptionHandler(Exception.class)
        public Map<String, Object> handle(Exception e){
            Map<String, Object> info = new HashMap<>() ;
            info.put("msg", e.getMessage()) ;
            info.put("time", LocalDateTime.now()) ;
            return info ;
        }
    }
    ```