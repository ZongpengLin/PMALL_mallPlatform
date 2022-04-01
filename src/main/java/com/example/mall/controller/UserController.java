package com.example.mall.controller;

import com.example.mall.consts.MallConst;
import com.example.mall.form.UserLoginForm;
import com.example.mall.form.UserRegisterForm;
import com.example.mall.pojo.User;
import com.example.mall.service.IUserService;
import com.example.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Objects;

import static com.example.mall.consts.MallConst.CURRENT_USER;
import static com.example.mall.enums.ResponseEnum.PARAM_ERROR;

@RestController
@Slf4j
public class UserController {
    @Autowired
    private IUserService userService;
    @PostMapping("/user/register")

    public ResponseVo register(@Valid @RequestBody UserRegisterForm userRegisterForm,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("注册提交的参数有误, {} {}",
                    Objects.requireNonNull(bindingResult.getFieldError()).getField(),
                    bindingResult.getFieldError().getDefaultMessage());
            return ResponseVo.error(PARAM_ERROR,bindingResult);   //见Response中的error方法
        }
        User user=new User();
        //  对象拷贝的方法
        BeanUtils.copyProperties(userRegisterForm, user);

        return userService.register(user);
       }
    @PostMapping("/user/login")
    // 设置session  登陆状态
    public ResponseVo<User> login(@Valid @RequestBody UserLoginForm userLoginForm ,
                                  BindingResult bindingResult,
                                  HttpSession session){
        if(bindingResult.hasErrors()){
            return ResponseVo.error(PARAM_ERROR,bindingResult);
        }
        ResponseVo<User> userResponseVo = userService.login(userLoginForm.getUsername(),userLoginForm.getPassword());
        // 设置Session
        // CURRENT_USER使用的是一个常量类
        session.setAttribute(CURRENT_USER,userResponseVo.getData());
        log.info("/login sessionId={}", session.getId());

        return userResponseVo;
    }

    @GetMapping("/user")
    public ResponseVo<User> userInfo(HttpSession session){
        log.info("/user sessionId={}",session.getId());
        User user = (User)session.getAttribute(MallConst.CURRENT_USER);
        return ResponseVo.success(user);
    }

    @PostMapping("/user/logout")
    /**
     * {@link org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory}
     * getSessionTimeoutInMinutes
     */
    public ResponseVo logout(HttpSession session){
        // sessionid
        log.info("user/logout sessionId={}", session.getId());
        // 判断登陆状态
//        User user = (User)session.getAttribute(CURRENT_USER);
//        if(user == null){
//            return ResponseVo.error(ResponseEnum.NEED_LOGIN);
//        }
        //登出操作
        session.removeAttribute(CURRENT_USER);
        return ResponseVo.success();
    }
}
