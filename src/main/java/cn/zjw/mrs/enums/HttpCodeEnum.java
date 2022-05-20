package cn.zjw.mrs.enums;

import lombok.Getter;

/**
 * @author zjw
 * @Classname HttpCodeEnum
 * @Date 2022/5/16 12:47
 * @Description
 */
@Getter
public enum  HttpCodeEnum {
    /**
     * 状态码 - 信息
     */
    SUCCESS(200,"操作成功"),
    NEED_LOGIN(401,"需要登录后操作"),
    NO_OPERATOR_AUTH(403,"无权限操作"),
    SYSTEM_ERROR(500,"出现错误"),
    USERNAME_EXIST(501,"用户名已存在"),
    PHONE_NUMBER_EXIST(502,"手机号已存在"),
    EMAIL_EXIST(503, "邮箱已存在"),
    REQUIRE_USERNAME(504, "必需填写用户名"),
    LOGIN_ERROR(505,"用户名或密码错误");

    final int code;
    final String msg;

    HttpCodeEnum(int code, String msg){
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
