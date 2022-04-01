package com.example.mall;

import com.example.mall.exception.UserLoginException;
import com.example.mall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.example.mall.consts.MallConst.CURRENT_USER;

@Slf4j
public class UserLoginInterceptor implements HandlerInterceptor  {

    /**
     * true 表示继续流程，false表示中断
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    // 返回的布尔型
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("preHandle...");
        User user = (User)request.getSession().getAttribute(CURRENT_USER);
        if(user == null){
            log.info("user == null");
            // 由于布尔型限制，所以采用统一异常处理返回json格式
            throw new UserLoginException();
//            return false;
//            return ResponseVo.error(ResponseEnum.NEED_LOGIN);
        }
        return true;

    }
}

