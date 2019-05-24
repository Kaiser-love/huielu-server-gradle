package com.ronghui.service.interceptor;

import com.ronghui.service.entity.User;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class UserInterceptor implements HandlerInterceptor {
    private static final String[] ALLOW_URL = {"/login",
            "/error",
            "/loginByTocken",
            "/getregisteragreement",
            "/register",
            "/loginwithauthcode",
            "/loginByTocken",
            "/logout",
            "/requesthead",
            "/checkusername",
            "/checkLogin",
            "/requestauthcode",
            "/appInit",
            "/checkUser",
            "/wechatRegister",
            "/download",
            "/shouldLogin"};

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
//        request.setAttribute();
        String url = request.getRequestURL().toString();
        User user = (User) session.getAttribute("user");
        for (String s : ALLOW_URL) {
            if (url.contains(s)) {
                return true;
            }
        }
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/shouldLogin");
            return false;
        }
        return true;
    }

}
