package com.yicj.auth.core.model;

import lombok.Data;

import java.util.List;

@Data
public class User {
    private String id ;
    private String userName ;
    private List<String> allowPermissionService ;
}
