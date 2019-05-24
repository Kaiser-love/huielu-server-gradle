package com.ronghui.service.controller;

import com.ronghui.service.entity.User;
import com.ronghui.service.entity.AuthCode;
import com.ronghui.server.files.network.ResCode;
import com.ronghui.server.network.Message;
import com.ronghui.service.service.UserService;
import com.ronghui.service.util.Util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpSession;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final long validTime = 10 * 60;


    // Constructor for UserController
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Internal Function

    /**
     * 移除session中所有信息
     *
     * @param session
     */
    private void removeAttributesInSession(HttpSession session) {
        log.debug("Remove all attribute in Session");
        Enumeration<String> names = session.getAttributeNames();
        try {
            while (true) {
                String name = names.nextElement();
                session.removeAttribute(name);
            }
        } catch (NoSuchElementException e) {
        }
        return;
    }

    private void replaceHeadUrl(User dbUser) {

    }

    /**
     * 检查验证码
     *
     * @param authcode
     * @param session
     * @return
     */
    public int checkAuthcode(AuthCode authcode, HttpSession session) {
        if (session.getAttribute("authcode") != null) {
            AuthCode sessionAuthCode = (AuthCode) session.getAttribute("authcode");
            log.debug("AuthCode is in Session-Phone:" + sessionAuthCode.getPhone() + "\tCode:" + sessionAuthCode.getCode() + "\tCreateTime:" + sessionAuthCode.getCreatetime());
            if (!sessionAuthCode.getPhone().equals(authcode.getPhone())) {
                // 验证码不匹配：手机号错误
                return 3;
            } else if (!sessionAuthCode.getCode().equals(authcode.getCode())) {
                // 验证码不匹配：验证码错误
                return 3;
            } else if (authcode.getCreatetime() - sessionAuthCode.getCreatetime() >= validTime * 1000) {
                // 验证码失效
                session.removeAttribute("authcode");
                return 4;
            }
            return 0;
        }
        log.debug("AuthCode isn't in Session:" + session.getId());
        // 验证码失效：不存在
        return 4;
    }

    // Request Mapping Functions
    // Login Register Logout

    /**
     * 登出
     *
     * @param session
     * @return
     */
    @PostMapping(value = "/logout")
    @ApiOperation("登出")
    public Message<String> logout(@ApiIgnore HttpSession session) {
        log.info("USER LOGOUT");
        removeAttributesInSession(session);
        Message<String> msg = new Message<>();
        msg.setResCode(ResCode.SUCCESS);
        return msg;
    }

    /**
     * 请求手机验证码
     *
     * @param phone
     * @param session
     * @return
     */
    @GetMapping(value = "/requestauthcode")
    @ApiOperation("请求手机验证码")
    public Message<Integer> requestAuthcode(@RequestParam(value = "phone", required = false) String phone,@ApiIgnore HttpSession session) {
        Message<Integer> msg = new Message<>();
        final int waitTime = 60;
        final int digit = 6;
        AuthCode authCode = null;
        long nowTime = System.currentTimeMillis();
        if (session.getAttribute("authcode") != null) {
            AuthCode sessionAuthCode = (AuthCode) session.getAttribute("authcode");
            if (nowTime - sessionAuthCode.getCreatetime() < validTime)
                authCode = sessionAuthCode;
        }
        if (authCode == null) {
            String code = Util.generateRandomCode(digit);
            authCode = new AuthCode(phone, code, nowTime);
        }
        if (Util.sendAuthCode(authCode.getPhone(), authCode.getCode())) {
            nowTime = System.currentTimeMillis();
            authCode.setCreatetime(nowTime);
            session.setAttribute("authcode", authCode);
            msg.setResCode(ResCode.SUCCESS);
            msg.setBody(waitTime);
        } else {
            log.error("短信接口错误!");
            msg.setResCode(ResCode.MESSAGE_INTERFACE_ERROR);
        }
        return msg;
    }

    /**
     * 手机号+密码登陆
     *
     * @param user
     * @param session
     * @return
     */
    @PostMapping(value = "/login")
    @ApiOperation("手机号+密码登陆")
    public Message<User> login(@RequestBody User user, @ApiIgnore HttpSession session) {
        Message<User> msg = new Message<>();
        if (userService.matchPasswordByPhone(user.getPhone(), user.getPassword())) {
            user = userService.findByPhone(user.getPhone());
            msg.setBody(user);
            msg.setResCode(ResCode.SUCCESS);
            session.setAttribute("user", user);
        } else {
            msg.setResCode(ResCode.LOGIN_ERROR);
        }
        return msg;
    }

    /**
     * 手机号+验证登陆或注册
     *
     * @param phone
     * @param authCode
     * @param session
     * @return
     */
    @GetMapping(value = "/loginwithauthcode")
    @ApiOperation("手机号+验证登陆或注册")
    public Message<User> loginWithAuthcode(@RequestParam("phone") String phone, @RequestParam("authcode") String authCode, @ApiIgnore HttpSession session) {
        Message<User> msg = new Message<>();
        long nowTime = System.currentTimeMillis();
        int code = checkAuthcode(new AuthCode(phone, authCode, nowTime), session);
        if (code != 0) {
            checkRes(msg, code);
        } else {
            User user = userService.findByPhone(phone);
            if (user == null) { // new user
                user = new User();
                String password = Util.stringToMD5("123456");
                user.setPhone(phone);
                user.setPassword(password);
                userService.save(user);
                user = userService.findByPhone(phone);
            }
            session.setAttribute("user", user);
            session.removeAttribute("authcode");
            msg.setResCode(ResCode.SUCCESS);
            msg.setBody(user);
        }
        return msg;
    }


    /*
     * 使用tocken与用户名实现客户端自动登录
     *
     * @param username
     * @param tocken
     * @param session
     * @return
     */
    @GetMapping(value = "/loginByTocken")
    @ApiOperation("使用tocken与用户名实现客户端自动登录")
    public Message<User> login(@RequestParam(value = "username", required = false) String phone, @RequestParam("tocken") String token, @ApiIgnore HttpSession session) {
        Message<User> msg = new Message<>();
        User dbUser = userService.findByTokenAndPhone(phone, token);
        if (dbUser != null) {
            dbUser.setToken(Util.createToken(dbUser.getPhone(), dbUser.getPassword()));
            userService.update(dbUser);
            session.setAttribute("user", dbUser);
            msg.setResCode(ResCode.SUCCESS);
            msg.setBody(dbUser);
        } else {
            msg.setResCode(ResCode.RELOGIN);
        }
        return msg;
    }

    /*
     * 获取用户头像
     */
    @PostMapping(value = "/requesthead")
    @ApiOperation("获取用户头像")
    public Message<String> requestHeadByPhone(@RequestParam("phone") String phone) {
        Message<String> msg = new Message<>();
        User user = userService.findByPhone(phone);
        if (user != null) {
            replaceHeadUrl(user);
            String head = user.getHead();
            msg.setBody(head);
            msg.setResCode(ResCode.SUCCESS);
        } else { // TODO: 返回默认图片
            msg.setResCode(ResCode.PHONE_NOT_EXIST);
        }
        return msg;
    }

    /**
     * 检查openid
     *
     * @param appid
     * @param secret
     * @param code
     * @param session
     * @return
     */
    @PostMapping(value = "/checkUser")
    @ApiOperation("检查openid")
    public Message<Map<String, Object>> checkOpenId(@RequestParam("appid") String appid, @RequestParam("secret") String secret, @RequestParam("code") String code, @ApiIgnore HttpSession session) {
        Message<Map<String, Object>> msg = new Message<>();
        Map<String, Object> body = new HashMap<>();
        String openid = Util.getOpenId(appid, secret, code);
        if (openid == null) {
            msg.setResCode(ResCode.INTERNAL_ERROR);
            return msg;
        }
        User user = userService.findByOpenid(openid);
        boolean hasPhone = (user != null);
        if (user != null) {
            body.put("user", user);
            session.setAttribute("user", user);
        }
        body.put("openid", openid);
        body.put("hasphone", hasPhone);
        body.put("sessionid", Util.base64Encode(session.getId()));
        msg.setBody(body);
        msg.setResCode(ResCode.SUCCESS);
        return msg;
    }

    /**
     * 微信登陆
     *
     * @param openid
     * @param phone
     * @param authCode
     * @param session
     * @return
     */
    @PostMapping(value = "/wechatRegister")
    @ApiOperation("微信登陆")
    public Message<User> wechatRegister(@RequestParam("openid") String openid,
                                        @RequestParam("phone") String phone,
                                        @RequestParam("authcode") String authCode,
                                        @RequestParam(value = "nickname",required = false) String nickName,
                                        @RequestParam(value = "head",required = false) String head,
                                        @ApiIgnore HttpSession session) {
        Message<User> msg = new Message<>();
        long nowTime = System.currentTimeMillis();
        int code = checkAuthcode(new AuthCode(phone, authCode, nowTime), session);
        if (code != 0) {
            checkRes(msg, code);
        } else {
            User user = userService.findByPhone(phone);
            if (user == null) { // new user

                user = new User();
                String password = Util.stringToMD5("123456");
                user.setPhone(phone);
                user.setOpenid(openid);
                user.setPassword(password);
                user.setHead(head);
                user.setNickname(nickName);
                userService.save(user);
            } else {
                user.setOpenid(openid);
                if (user.getHead() == null || "".equals(user.getHead())) {
                    user.setHead(nickName);
                }
                if (user.getNickname() == null || "".equals(user.getNickname())) {
                    user.setNickname(nickName);
                }
                userService.updateOpenId(user.getUid(), openid);
            }
            user = userService.findByOpenid(openid);
            session.setAttribute("user", user);
            session.removeAttribute("authcode");
            msg.setResCode(ResCode.SUCCESS);
            msg.setBody(user);
        }
        return msg;

    }

    // User Info

    /**
     * 更新个人信息
     *
     * @param user
     * @param authcode
     * @param session
     * @return
     */
    @PostMapping(value = "/updateuserinfo")
    @ApiOperation("更新个人信息")
    public Message<User> updateUserInfo(@RequestBody User user, @RequestParam(value = "authcode", required = false) String authcode,@ApiIgnore HttpSession session) {
        Message<User> msg = new Message<>();
        if (session.getAttribute("user") == null) {
            msg.setResCode(ResCode.RELOGIN);
            return msg;
        }
        User dbUser = userService.findById(((User) session.getAttribute("user")).getUid());
        if (authcode != null && authcode != "" && user.getPhone() != null && user.getPhone() != "") {
            long nowTime = System.currentTimeMillis();
            int code = checkAuthcode(new AuthCode(user.getPhone(), authcode, nowTime), session);
            if (code != 0) {
                checkRes(msg, code);
                return msg;
            } else {
                User u = userService.findByPhone(user.getPhone());
                if (u != null && u.getUid() != dbUser.getUid()) {
                    msg.setResCode(ResCode.PHONE_EXIST);
                    return msg;
                } else if (u == null) {
                    dbUser.setPhone(user.getPhone());
                }
                msg.setResCode(ResCode.SUCCESS);
            }
        }
        if (user.getNickname() != null && !user.getNickname().equals(""))
            dbUser.setNickname(user.getNickname());
        if (user.getSex() != -1) {
            dbUser.setSex(user.getSex());
        }
        if (user.getBirthday() != null && !user.getBirthday().equals(""))
            dbUser.setBirthday(user.getBirthday());
        if (user.getQq() != null && !user.getQq().equals(""))
            dbUser.setQq(user.getQq());
        if (user.getEmail() != null && !user.getEmail().equals(""))
            dbUser.setEmail(user.getEmail());
        if (user.getCompany() != null && !user.getCompany().equals(""))
            dbUser.setCompany(user.getCompany());
        if (user.getTitle() != null && !user.getTitle().equals(""))
            dbUser.setTitle(user.getTitle());
        if (user.getIndustry() != null && !user.getIndustry().equals(""))
            dbUser.setIndustry(user.getIndustry());
        if (user.getHead() != null && !user.getHead().equals("")) {
            dbUser.setHead(user.getHead());
        }
        userService.update(dbUser);
        session.setAttribute("user", dbUser);
        if (session.getAttribute("authcode") != null)
            session.removeAttribute("authcode");
        msg.setBody(dbUser);
        return msg;

    }

    private void checkRes(Message<User> msg, int code) {
        if (code == 3) {
            msg.setResCode(ResCode.AUTHCODE_ERROR);
        } else if (code == 4) {
            msg.setResCode(ResCode.AUTHCODE_INVALID);
        } else {
            log.error("未知代码" + code);
            msg.setResCode(ResCode.INTERNAL_ERROR);
        }
    }

    /*
     * 更新密码
     */
    @PostMapping(value = "/updatepassword")
    @ApiOperation("更新密码")
    public Message<String> updatePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword,@ApiIgnore HttpSession session) {
        Message<String> msg = new Message<>();
        if (session.getAttribute("user") == null) {
            msg.setResCode(ResCode.RELOGIN);
            return msg;
        }
        User user = (User) session.getAttribute("user");
        if (userService.matchPasswordByPhone(user.getPhone(), oldPassword)) {
            user.setPassword(newPassword);
            userService.update(user);
            msg.setResCode(ResCode.SUCCESS);
        } else {
            msg.setResCode(ResCode.PASS_ERROR);
        }
        return msg;
    }

    /**
     * 提示重新登录
     *
     * @return
     */
    @GetMapping(value = "/shouldLogin")
    @ApiOperation("提示重新登录")
    public Message<String> shouldLogin() {
        log.debug("重新登陆");
        Message<String> msg = new Message<>();
        msg.setResCode(ResCode.RELOGIN);
        return msg;
    }
}
