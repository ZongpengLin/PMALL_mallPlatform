package com.example.mall.service.impl;

import com.example.mall.MallApplicationTests;
import com.example.mall.enums.ResponseEnum;
import com.example.mall.enums.RoleEnum;
import com.example.mall.pojo.User;
import com.example.mall.service.IUserService;
import com.example.mall.vo.ResponseVo;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class UserServiceImplTest extends MallApplicationTests {
    public static final String USERNAME = "jacky";
    public static final String PASSWORD = "1234567";
    @Autowired
    private IUserService userService;

    @Test
    //@Before
    public void register() {
        User user = new User("jacky", "1234567", "jacky@qq.com", RoleEnum.CUSTOMER.getCode());
        userService.register(user);
    }
    @Test
    public void login(){
        register();
        ResponseVo<User> responseVo = userService.login(USERNAME, PASSWORD);
       
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }
}