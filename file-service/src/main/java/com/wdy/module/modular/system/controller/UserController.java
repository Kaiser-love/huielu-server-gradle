package com.wdy.module.modular.system.controller;

import com.wdy.module.core.aop.CurrentUser;
import com.wdy.module.core.common.response.ResponseHelper;
import com.wdy.module.core.common.response.ResultBean;
import com.wdy.module.core.common.utils.ContextUtil;
import com.wdy.module.core.common.utils.JWTTokenUtil;
import com.wdy.module.core.log.LogManager;
import com.wdy.module.core.log.factory.LogTaskFactory;
import com.wdy.module.core.redis.RedisUtil;
import com.wdy.module.core.shiro.ShiroKit;
import com.wdy.module.core.shiro.ShiroUser;
import com.wdy.module.modular.system.entity.User;
import com.wdy.module.modular.system.model.LoginUser;
import com.wdy.module.modular.system.entity.TUser;
import com.wdy.module.modular.system.service.TUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.ImmutableMap;
import com.wdy.module.modular.system.service.UserService;
import io.swagger.annotations.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.io.Serializable;
import java.util.*;

import static cn.stylefeng.roses.core.util.HttpContext.getIp;

/**
 * @program: guns
 * @description:
 * @author: dongyang_wu
 * @create: 2019-05-19 13:14
 */
@RestController
@Api(description = "用户API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    @Autowired
    private TUserService tUserService;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtil redisUtil;

    @ApiOperation(value = "user登录")
    @PostMapping("/user/login")
    public ResponseEntity<ResultBean> login(@RequestBody @ApiParam(value = "用户信息json格式") LoginUser loginUser) {
        User user = userService.getBaseMapper().selectOne(new QueryWrapper<User>().eq("ACCOUNT", loginUser.getUsername()));
        if (user != null) {
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(loginUser.getUsername(), loginUser.getPassword());
            SecurityUtils.getSubject().login(usernamePasswordToken);
            HttpHeaders responseHeaders = new HttpHeaders();
            Session session = SecurityUtils.getSubject().getSession();
            Serializable id = session.getId();
            session.setTimeout(5 * 60000);
            Map<String, Object> claims = ImmutableMap.<String, Object>builder().put("id", id.toString()).put("userName", loginUser.getUsername()).build();
            String token = id + " " + JWTTokenUtil.createJWTToken(claims, (long) (5 * 60000));
            redisUtil.sentinelSet(token, user, (long) (5 * 60000));
            responseHeaders.set(ContextUtil.AUTHORIZATION, token);
            responseHeaders.set("Access-Control-Expose-Headers", ContextUtil.AUTHORIZATION);
            //登录成功，记录登录日志
            ShiroUser shiroUser = ShiroKit.getUserNotNull();
            LogManager.me().executeLog(LogTaskFactory.loginLog(shiroUser.getId(), getIp()));
            ShiroKit.getSession().setAttribute("sessionFlag", true);
            return new ResponseEntity<>(ResultBean.success(user), responseHeaders, HttpStatus.OK);
        } else {
            //登陆失败
            return ResponseHelper.BadRequest("用户名或密码错误");
        }
    }

    @ApiOperation(value = "获取当前用户信息")
    @GetMapping("/user/currentUser")
    public ResponseEntity<ResultBean> getUser(@ApiIgnore @CurrentUser TUser user) {
        return ResponseHelper.OK(user);
    }


}