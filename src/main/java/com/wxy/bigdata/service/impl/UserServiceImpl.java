package com.wxy.bigdata.service.impl;

import com.wxy.bigdata.entity.User;
import com.wxy.bigdata.mapper.UserMapper;
import com.wxy.bigdata.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public User selectByPrimaryKey(Integer id) {
        return this.userMapper.selectByPrimaryKey(id);
    }
}
