package com.yicj.study.filter;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Order(1)
@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private RateLimiter rateLimiter =  RateLimiter.create(1) ;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
        HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (rateLimiter.tryAcquire()){
            filterChain.doFilter(request, response);
            return;
        }
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.getWriter().write("too many request !!!");
        response.getWriter().flush();
    }
}
