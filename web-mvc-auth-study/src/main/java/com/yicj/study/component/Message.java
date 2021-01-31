package com.yicj.study.component;

import lombok.Data;

@Data
public class Message<T> {
    private Integer code ;
    private String msg ;
    private T data ;

    public static <T> Message<T> success(T data){
        Message<T> message = new Message<>() ;
        message.setCode(200);
        message.setMsg(null);
        message.setData(data);
        return message ;
    }

    public static <T> Message<T> fail(String msg){
        Message<T> message = new Message<>() ;
        message.setCode(500);
        message.setMsg(msg);
        message.setData(null);
        return message ;
    }
}

