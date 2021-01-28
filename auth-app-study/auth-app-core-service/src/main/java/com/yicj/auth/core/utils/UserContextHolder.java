package com.yicj.auth.core.utils;

import com.yicj.auth.core.model.User;

public class UserContextHolder {

    private static final ThreadLocal<User> local = new ThreadLocal<>() ;

    public static User get(){
        return local.get() ;
    }

    public static void set(User user){
        local.set(user);
    }

    public static void shutdown(){
        local.remove();
    }

}
