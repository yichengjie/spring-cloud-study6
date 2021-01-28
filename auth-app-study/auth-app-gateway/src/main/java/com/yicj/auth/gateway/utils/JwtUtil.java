package com.yicj.auth.gateway.utils;

import com.yicj.auth.gateway.exception.PermissionException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang.StringUtils;

import javax.naming.NoPermissionException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class JwtUtil {

    private static final String TOKEN_PREFIX = "XXX" ;
    private static final String SECRET = "123" ;
    public static final String HEADER_AUTH = "xtoken" ;

    public static String generateToken(String username){
        HashMap<String, Object> map = new HashMap<>() ;
        map.put("id", new Random().nextInt()) ;
        map.put("user", username) ;
        String jwt = Jwts.builder()
                .setSubject("user info").setClaims(map)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        return TOKEN_PREFIX + " " + jwt;
    }

    public static Map<String,String> validateToken(String token){
        if (token != null){
            HashMap<String, String> map = new HashMap<>() ;
            Map<String, Object> body = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX + " ", ""))
                    .getBody() ;
            String id = String.valueOf(body.get("id")) ;
            String username = (String) body.get("user") ;
            map.put("id", id) ;
            map.put("user", username) ;
            if (StringUtils.isEmpty(username)){
                throw new PermissionException("user is error, please check !") ;
            }
            return map ;
        }
        throw new PermissionException("token is error , please check !") ;
    }
}
