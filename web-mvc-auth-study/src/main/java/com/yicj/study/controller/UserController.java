package com.yicj.study.controller;

import com.yicj.study.model.entity.User;
import com.yicj.study.repository.UserRepository;
import com.yicj.study.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository repository ;
    @Autowired
    private UserService service ;

    @PostMapping
    public User addUser(@RequestBody @Validated User user){
        return repository.save(user) ;
    }

    @PostMapping("/login")
    public void login(String username, String password, HttpServletRequest request){
        // 防止session固定攻击
        HttpSession session = request.getSession(false);
        if (session !=null){
            session.invalidate();
        }
        session = request.getSession(true);
        User user = service.login(username, password);
        session.setAttribute("user", user);
    }

    @GetMapping("/logout")
    public void logout(HttpSession session){
        session.invalidate();
    }
}
