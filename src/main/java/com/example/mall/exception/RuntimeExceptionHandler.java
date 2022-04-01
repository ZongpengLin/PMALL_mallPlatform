package com.example.mall.exception;

import com.example.mall.enums.ResponseEnum;
import com.example.mall.vo.ResponseVo;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Objects;

import static com.example.mall.enums.ResponseEnum.ERROR;

@ControllerAdvice
public class RuntimeExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    // ResponseBody 返回json格式
    @ResponseBody
    // 不显示状态码
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseVo handle(RuntimeException e) {
        return ResponseVo.error(ERROR, e.getMessage());
    }

    //登陆异常
    @ExceptionHandler(UserLoginException.class)
    @ResponseBody
    public ResponseVo userLoginHandle() {
        return ResponseVo.error(ResponseEnum.NEED_LOGIN);
    }

    //表单统一验证异常chul
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseVo notValidExceptionHandle(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();
        Objects.requireNonNull(bindingResult.getFieldError());
        return ResponseVo.error(ResponseEnum.PARAM_ERROR,
                bindingResult.getFieldError().getField() + " "+ bindingResult.getFieldError().getDefaultMessage());

    }
}
