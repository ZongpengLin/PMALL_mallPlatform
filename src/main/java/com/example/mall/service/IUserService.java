package com.example.mall.service;

import com.example.mall.pojo.User;
import com.example.mall.vo.ResponseVo;

public interface IUserService {
    /**
     *  service 层完成的是业务逻辑
     *  包含了以下功能块：
     *  1。注册
     */
    ResponseVo<User> register(User user);

    /**
     *  2。登陆
     */
    ResponseVo<User> login(String username,String password);

}
