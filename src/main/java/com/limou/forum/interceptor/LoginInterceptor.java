package com.limou.forum.interceptor;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.limou.forum.config.AppConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author 小李哞哞
 * @date 2023/8/29
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    private String defaultURL;

    @Value("${forum.login.url}")
    public void setDefaultURL(String defaultURL) {
        // 确保路径以"/"开头
        this.defaultURL = StrUtil.addPrefixIfNot(defaultURL, "/");
    }

    /**
     * 前置处理(对请求的预处理)
     *
     * @return true: 继续流程 <br/> false: 流程中断
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取Session对象
        HttpSession session = request.getSession(false);
        // 判断Session是否有效
        if (ObjUtil.isNotEmpty(session)
                && ObjUtil.isNotEmpty(session.getAttribute(AppConfig.USER_SESSION))) {
            // 用户为已登录状态，放行
            return true;
        }
        // 校验不通过，跳转到登录界面
        response.sendRedirect(defaultURL);
        // 流程中断
        return false;
    }
}
