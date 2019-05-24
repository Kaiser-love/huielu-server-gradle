//package com.service.controller;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Set;
//
//public class LoginSuccessHandle implements AuthenticationSuccessHandler {
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request,
//                                        HttpServletResponse response, Authentication authentication) throws IOException {
//        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
//        String path = request.getContextPath();
//        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
//        if (roles.contains("ROLE_TEACHER")) {
//            response.sendRedirect(basePath + "teacher");
//            return;
//        }
//        response.sendRedirect(basePath + "student");
//    }
//}