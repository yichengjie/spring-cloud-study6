package com.yicj.study.model.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;
    @NotBlank(message = "用户名不能为空")
    private String username ;
    @NotBlank(message = "密码不能为空")
    private String password ;
    private String roles ;

    public boolean hasPermission(String method) {
        boolean result = false ;
        if (StringUtils.equalsIgnoreCase("get", method)){
            result = StringUtils.contains(this.getRoles(),"ROLE_USER") ;
        }else {
            result = StringUtils.contains(this.getRoles(), "ROLE_ADMIN") ;
        }
        return result ;
    }
}
