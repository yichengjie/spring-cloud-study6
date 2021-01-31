package com.yicj.study.service.impl;

import com.yicj.study.model.entity.User;
import com.yicj.study.repository.UserRepository;
import com.yicj.study.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repository ;
    @Override
    public User login(String username, String password) {
        User user = repository.findByUsername(username);
        if (user == null){
            throw new RuntimeException("user not exist !") ;
        }
        if (!StringUtils.equals(password, user.getPassword())){
            throw new RuntimeException("password invalid !") ;
        }
        return user ;
    }
}
