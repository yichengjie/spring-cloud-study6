package com.yicj.study.filter;

import com.yicj.study.model.entity.User;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AclInterceptor implements HandlerInterceptor {

    private String [] permitUrls = {"/users/login"} ;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果在不需要权限认证集合中
        if (ArrayUtils.contains(permitUrls, request.getRequestURI())){
            return true ;
        }
        boolean result = true ;
        User user = (User) request.getSession().getAttribute("user");
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
