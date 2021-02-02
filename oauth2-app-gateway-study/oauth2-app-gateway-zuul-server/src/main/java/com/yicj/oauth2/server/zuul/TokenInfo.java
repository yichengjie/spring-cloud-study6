package com.yicj.oauth2.server.zuul;

import lombok.Data;

import java.util.Date;

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