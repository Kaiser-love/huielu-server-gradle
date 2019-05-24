package com.wdy.module.core.common.utils;


import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.wdy.module.core.common.exception.ResultEnum;
import com.wdy.module.core.context.SpringContextUtil;
import com.wdy.module.core.redis.RedisUtil;
import com.wdy.module.modular.system.entity.TUser;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 访问用户信息
 *
 * @author dongyang_wu
 * @date 2019/4/12 12:22
 */
public class ContextUtil {
    public static final String AUTHORIZATION = "HUIELU-TOKEN";
    private static final String BASE_USERNAME = "未携带token用户";
    private static TUser user = new TUser(BASE_USERNAME);

    public static TUser getUser() {
        RedisUtil redisUtil = (RedisUtil) SpringContextUtil.getBean("RedisUtil");
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String token = request.getHeader(AUTHORIZATION);
            if (!StringUtils.isEmpty(token)) {
                user = (TUser) redisUtil.sentinelGet(token, TUser.class);
            }
        } catch (Exception e) {
        }
        return user;
    }

    public static Boolean isUser() {
        return !BASE_USERNAME.equals(user.getUserName());
    }

    public static TUser getUserByToken(HttpServletRequest request) {
        String id = request.getHeader(AUTHORIZATION);
        RedisUtil redisUtil = (RedisUtil) SpringContextUtil.getBean("RedisUtil");
        return (TUser) redisUtil.sentinelGet(id, TUser.class);
    }

    public static TUser judgeUser() {
        getUser();
        if (!ContextUtil.isUser()) {
            throw new ServiceException(ResultEnum.USER_NOT_EXIST);
        }
        return user;
    }
}
