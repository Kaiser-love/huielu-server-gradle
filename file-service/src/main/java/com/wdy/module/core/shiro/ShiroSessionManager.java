package com.wdy.module.core.shiro;

import com.wdy.module.core.common.response.ResponseHelper;
import com.wdy.module.core.common.utils.ContextUtil;
import com.wdy.module.core.common.utils.JWTTokenUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Map;

/**
 * Description:shiro框架 自定义session获取方式
 * 可自定义session获取规则。这里采用ajax请求头authToken携带sessionId的方式
 *
 * @author dongyang_wu
 * @create 2018-05-24 10:04
 **/
public class ShiroSessionManager extends DefaultWebSessionManager {


    private static final String REFERENCED_SESSION_ID_SOURCE = "Stateless request";

    public ShiroSessionManager() {
        super();
    }

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        String id = WebUtils.toHttp(request).getHeader(ContextUtil.AUTHORIZATION);
        if (StringUtils.isEmpty(id)) {
            //如果没有携带id参数则按照父类的方式在cookie进行获取
            return super.getSessionId(request, response);
        } else {
            String sessionId = setUserBean(request, id);
            //如果请求头中有 authToken 则其值为sessionId
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, REFERENCED_SESSION_ID_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return sessionId;
        }
    }

    private String setUserBean(ServletRequest request, String id) {
        String[] token = id.split(" ");
        if (token.length < 2)
            return null;
        Map map = JWTTokenUtil.parseJWToken(token[1]);
        String userName = (String) map.get("userName");
        String sessionId = (String) map.get("id");
        if (userName != null && sessionId.equals(token[0])) {
            request.setAttribute("currentUser", userName);
            return sessionId;
        }
        return null;
    }

    /**
     * 非法url返回身份错误信息
     */
    private void responseError(ServletRequest request, ServletResponse response) {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("utf-8");
            out = response.getWriter();
            response.setContentType("application/json; charset=utf-8");
            out.print(JSONObject.toJSONString(ResponseHelper.BadRequest("未授权")));
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}