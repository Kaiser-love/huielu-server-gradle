package com.ronghui.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ronghui.server.files.network.ResCode;
import com.ronghui.server.network.Message;
import com.ronghui.service.entity.Content;
import com.ronghui.service.entity.User;
import com.ronghui.service.service.ContentService;
import com.ronghui.service.service.PPTService;
import com.ronghui.service.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@Slf4j
@ApiIgnore
public class MainController {
    private final ContentService contentService;
    private final UserService userService;
    private final PPTService pptService;

    @Autowired
    public MainController(ContentService contentService, UserService userService, PPTService pptService) {
        this.contentService = contentService;
        this.userService = userService;
        this.pptService = pptService;
    }

    @RequestMapping(value = "/index")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/test", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Message<String> test(HttpServletRequest request) {

        String url = "";
        url = request.getScheme() + "://" + request.getServerName()
                + ":" + request.getServerPort()
                + request.getServletPath();
        if (request.getQueryString() != null) {
            url += "?" + request.getQueryString();
        }
        Message<String> msg = new Message<>();
        msg.setBody(url);
        msg.setResCode(ResCode.SUCCESS);
        return msg;
    }

    @RequestMapping(value = "/appInit", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Message<HashMap<String, Object>> initApp(String type, HttpSession session) {
        Message<HashMap<String, Object>> msg = new Message<>();
        HashMap<String, Object> hashMap = new HashMap<>();
        User user = ((User) session.getAttribute("user"));
        hashMap.put("login", user != null);
        List<Content> contents = contentService.findByType(type);
        contents.forEach(content -> hashMap.put(content.getName(), content.getData()));
        if (user != null) {
            try {
                User dbUser = userService.findById(user.getUid());
                userService.cleanAuthorEntity(dbUser);
                dbUser.setPptCount(pptService.countPPTs(dbUser.getUid()));
                hashMap.put("user", dbUser);
            } catch (Exception ignored) {

            }
        }
        msg.setBody(hashMap);
        msg.setResCode(ResCode.SUCCESS);
        return msg;
    }
}
