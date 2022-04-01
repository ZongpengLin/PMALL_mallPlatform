package com.example.mall.vo;

import com.example.mall.enums.ResponseEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.validation.BindingResult;

import java.util.Objects;

/**
 * 注册成功 ，返回json
 */
@Data
//为了不返回data内容
// @JsonSerialize
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ResponseVo<T> {

    private Integer status;

    private String msg;

    // 为了返回 data , 它的json 形式很难通过基本数据类型来表示，这里我们采用泛型
    private T data;

    private ResponseVo(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public ResponseVo(Integer status, T data) {
        this.status = status;
        this.data = data;
    }

    public static <T> ResponseVo<T> successByMsg(String msg){
        return new ResponseVo<>(ResponseEnum.SUCCESS.getCode(),msg);
    }


    // status 、data
    public static <T> ResponseVo<T> success(T data) {
        // return new ResponseVo<T>(0, msg);
        // 用枚举方式替换一下
        return new ResponseVo<>(ResponseEnum.SUCCESS.getCode(), data);
    }

    public static <T> ResponseVo<T> success() {
        // 用枚举方式替换一下
        return new ResponseVo<>(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getDesc());
    }

    public static <T> ResponseVo<T> error(ResponseEnum responseEnum) {
        // 用枚举方式替换一下
        return new ResponseVo<>(responseEnum.getCode(), responseEnum.getDesc());
    }

    //返回具体错误信息msg  （表单验证）
    public static <T> ResponseVo<T> error(ResponseEnum responseEnum, String msg) {
        // 用枚举方式替换一下
        return new ResponseVo<>(responseEnum.getCode(), msg);
    }

    // 将表单错误信息以参数的形式传进来
    public static <T> ResponseVo<T> error(ResponseEnum responseEnum, BindingResult bindingResult) {
        // 用枚举方式替换一下
        return new ResponseVo<>(responseEnum.getCode(),
                Objects.requireNonNull(bindingResult.getFieldError()).getField() + " "+ bindingResult.getFieldError().getDefaultMessage());
    }

}
