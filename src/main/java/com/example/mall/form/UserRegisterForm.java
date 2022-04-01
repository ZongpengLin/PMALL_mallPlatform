package com.example.mall.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 表单对象 form
 */
@Data
public class UserRegisterForm {
    //@NotEmpty  用于集合
    //@NotBlank 用于 String 判断空格
    //@NotBlank(message ="用户名不能为空")
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String email;
}
