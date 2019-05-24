package com.ronghui.server.files.network;

public enum ResCode {
    SUCCESS(0, "成功"),
    PHONE_NOT_EXIST(1, "手机号不存在"),
    LOGIN_ERROR(2, "账号或密码错误"),
    AUTHCODE_ERROR(3, "手机号或验证码不匹配"),
    AUTHCODE_INVALID(4, "验证码失效"),
    MESSAGE_INTERFACE_ERROR(5, "短信接口错误"),
    RELOGIN(6, "请重新登陆"),
    INTERNAL_ERROR(7, "内部错误"),
    PASS_ERROR(8, "密码错误"),					// 用于更新密码时旧密码错误
    PHONE_EXIST(9, "手机号已经存在"),
    ;

    int code;
    String msg;
    ResCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
