package com.yicj.auth.gateway.filter;

import com.yicj.auth.gateway.exception.PermissionException;
import com.yicj.auth.gateway.utils.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;

@Component
public class AuthFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Route gatewayUrl = exchange.getRequiredAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        URI uri = gatewayUrl.getUri();
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        String token = headers.getFirst(JwtUtil.HEADER_AUTH);
        Map<String, String> userMap = JwtUtil.validateToken(token);
        ServerHttpRequest.Builder mutate = request.mutate();
        String userName = userMap.get("user") ;
        if (userName.equals("admin") || userName.equals("spring") || userName.equals("cloud")){
            mutate.header("x-user-id", userMap.get("id")) ;
            mutate.header("x-user-name", userName) ;
            mutate.header("x-user-serviceName", uri.getHost()) ;
        }else {
            throw new PermissionException("user not exist, please check") ;
        }
        ServerHttpRequest newRequest = mutate.build();
        return chain.filter(exchange.mutate().request(newRequest).build());
    }
}
