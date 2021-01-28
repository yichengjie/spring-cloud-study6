package com.yicj.auth.core.utils;

import com.yicj.auth.core.model.User;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class UserPermissionUtil {

    // 模拟权限校验,可以根据自己项目需要定制不同的策略，如查询数据库获取具体的菜单url或则角色等
    public static boolean verify(User user, HttpServletRequest request) {
        String url = request.getHeader("x-user-serviceName");
        if (StringUtils.isEmpty(user)){
            return false ;
        }else {
            List<String> list = user.getAllowPermissionService();
            for (String item : list){
                if (url.equalsIgnoreCase(item)){
                    return true ;
                }
            }
            return false ;
        }
    }

    // 模拟权限赋值，可以根据自己项目需要定制不同的策略，如查询数据库获取具体的菜单url或则角色等
    public static void permission(User user){
        List<String> list = new ArrayList<>() ;
        if ("admin".equalsIgnoreCase(user.getUserName())){
            list.add("client-service");
            list.add("provider-service");
        }else if ("spring".equalsIgnoreCase(user.getUserName())){
            list.add("client-service");
        }
        user.setAllowPermissionService(list);
    }
}
