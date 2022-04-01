package com.example.mall.service.impl;


import com.example.mall.dao.UserMapper;
import com.example.mall.enums.RoleEnum;
import com.example.mall.pojo.User;
import com.example.mall.service.IUserService;
import com.example.mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

import static com.example.mall.enums.ResponseEnum.*;

@Service
public class UserServiceImpl implements IUserService {
    // 先写一个接口，再写一个实现类， 是为了用这一个接口起到约束的作用
    @Autowired
    private UserMapper userMapper;

    /**
     * service 层完成的是业务逻辑
     * 包含了以下功能块：
     * 1。注册
     *
     * @param user
     */
    @Override
    public ResponseVo<User> register(User user) {
        // 功能实现
        // userName不能重复， email 不能重复

        int countByUsername = userMapper.countByUsername(user.getUsername());
        if (countByUsername > 0) {
//            throw new RuntimeException("该username已注册");
            return ResponseVo.error(USERNAME_EXIST);
        }
        int countByEmail = userMapper.countByEmail(user.getEmail());
        if (countByEmail > 0) {
            //  throw new RuntimeException("该email已注册");
            return ResponseVo.error(EMAIL_EXIST);
        }

        // 需要默认一个用户角色设置
        user.setRole(RoleEnum.CUSTOMER.getCode());
        // MD5 加密（Spring里带上了该 MD5 散列摘要算法）
        //写入数据库前先加密处理(留心)
//        String s = DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8);
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8)));
        // 开始写入数据库
        int resultCount = userMapper.insertSelective(user);
        if (resultCount == 0) {
            // throw new RuntimeException("注册失败！");
            return ResponseVo.error(ERROR);
        }
        return ResponseVo.success();
    }

    @Override
    public ResponseVo<User> login(String username, String password) {
        // 不推荐联合使用字段查询
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            //用户不存在(返回：用户名或密码错误)
            return ResponseVo.error(USERNAME_OR_PASSWORD_ERROR);
        }
        // equalsIgnoreCase 忽略大小写
        if (!user.getPassword().equalsIgnoreCase(
                DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)))) {
            //密码错误(返回：用户名或密码错误)
            return ResponseVo.error(USERNAME_OR_PASSWORD_ERROR);
        }
        //  用户名或者密码错误
        // 不能返回用户密码json
        user.setPassword("");
        return ResponseVo.success(user);
    }

    private void error() {
        throw new RuntimeException("意外错误");
    }

}
