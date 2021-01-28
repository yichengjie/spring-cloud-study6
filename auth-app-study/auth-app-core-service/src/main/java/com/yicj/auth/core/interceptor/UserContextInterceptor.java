package com.yicj.auth.core.interceptor;

import com.alibaba.fastjson.JSON;
import com.yicj.auth.core.model.User;
import com.yicj.auth.core.utils.UserContextHolder;
import com.yicj.auth.core.utils.UserPermissionUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.naming.NoPermissionException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// controller 拦截器
public class UserContextInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = getUser(request) ;
        UserPermissionUtil.permission(user);
        if (!UserPermissionUtil.verify(user, request)){
            response.setHeader("Content-Type", "application/json");
            String jsonString = JSON.toJSONString("no permission access service, please check");
            response.getWriter().write(jsonString);
            response.getWriter().flush();
            response.getWriter().close();
            throw new NoPermissionException("no permission access service, please check ") ;
        }
        UserContextHolder.set(user) ;
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContextHolder.shutdown() ;
    }

    // 从request中获取用户信息
    private User getUser(HttpServletRequest request) {
        String id = request.getHeader("x-user-id");
        String username = request.getHeader("x-user-name") ;
        User user = new User() ;
        user.setId(id);
        user.setUserName(username);
        return user ;
    }
}
