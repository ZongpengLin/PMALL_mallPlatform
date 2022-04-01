package com.example.mall;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //   WebMvcConfigurer.super.addInterceptors(registry);
        //默认对所有的URL进行拦截
       registry.addInterceptor(new UserLoginInterceptor())
               .addPathPatterns("/**")
               .excludePathPatterns("/error","/user/login","/user/register","/categories","/products","/products/*");
    }
}
