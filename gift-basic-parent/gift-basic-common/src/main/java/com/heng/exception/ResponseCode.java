package com.heng.exception;

/**
 * @author shkstart
 * @create 2023-04-25 19:02
 * @Description: 响应码枚举类
 */

public enum ResponseCode {

    RESPONSE_CODE_200(200,"操作成功"),
    RESPONSE_CODE_400(400,"参数不正确"),
    RESPONSE_CODE_400_TENANT_EXIST(40001,"店铺已经存在"),
    RESPONSE_CODE_400_TENANT_ADMIN_EXIST(40002,"您已经有店铺存在,请直接登录后使用或者添加新的店铺"),
    RESPONSE_CODE_400_TENANT_ADMIN_PASSWORD_NOT_EQUALS(40003,"两次密码不一致,请重新输入"),
    RESPONSE_CODE_400_ONSALE_ERROR(40004,"上架失败"),
    RESPONSE_CODE_500(500,"系统繁忙,请稍微再试");

    private Integer code;
    private String message;

    //构造器
    ResponseCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
