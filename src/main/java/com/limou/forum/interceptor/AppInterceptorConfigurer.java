package com.limou.forum.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author 小李哞哞
 * @date 2023/8/29
 */
@Configuration
public class AppInterceptorConfigurer implements WebMvcConfigurer {

    @Resource
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加用户登录拦截器
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns("/sign-in.html") // 排除登录页面
                .excludePathPatterns("/sign-up.html") // 排除注册页面
                .excludePathPatterns("/user/login") // 排除登录API接口
                .excludePathPatterns("/user/register") // 排除注册API接口
                .excludePathPatterns("/user/logout") // 排除退出登录接口
                .excludePathPatterns("/swagger*/**") // 排除swagger下所有
                .excludePathPatterns("/v3*/**") // 排除登录v3下所有，和swagger相关
                .excludePathPatterns("/doc*/**") // 排除knife4j接口文档
                .excludePathPatterns("/webjars*/**") // 排除knife4j接口文档相关
                .excludePathPatterns("/dist/**") // 排除所有静态文件
                .excludePathPatterns("/image/**")
                .excludePathPatterns("/**.ico")
                .excludePathPatterns("/js/**");
    }
}
