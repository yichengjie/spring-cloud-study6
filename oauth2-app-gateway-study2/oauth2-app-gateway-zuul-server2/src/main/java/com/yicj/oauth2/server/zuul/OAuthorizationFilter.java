package com.yicj.oauth2.server.zuul;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class OAuthorizationFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null){
            if (!isOauthServerRequest(request)){
                log.info("audit log update fail 401");
                handleError(401, currentContext) ;
            }
            return null ;
        }else {// 如果不为空
            String principal = (String) authentication.getPrincipal();
            currentContext.addZuulRequestHeader("username", principal);
            return null;
        }
    }

    private void handleError(int status, RequestContext ctx) {
        ctx.getResponse().setContentType("application/json");
        ctx.setResponseStatusCode(status);
        ctx.setResponseBody("{\"message\":\"audit fail !\"}");
        ctx.setSendZuulResponse(false);
    }

    private boolean isOauthServerRequest(HttpServletRequest request){
        String uri = request.getRequestURI();
        return StringUtils.startsWith(uri, "/token") ;
    }
}
