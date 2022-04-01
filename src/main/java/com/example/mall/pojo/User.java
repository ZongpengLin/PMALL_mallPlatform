package com.example.mall.pojo;

import lombok.Data;

import java.util.Date;
@Data
public class User {
    private Integer id;

    private String username;

    private String password;

    private String email;

    private String phone;

    private String question;

    private String answer;

    private Integer role;

    private Date createTime;

    private Date updateTime;

    public User() {
    }

    public User(String username, String password, String email, int role) {
        this.username = username;
        this.password = password;
        this.email = email;

        // 0,1 两种角色选项，我们想到使用枚举
        this.role =role;
    }
}