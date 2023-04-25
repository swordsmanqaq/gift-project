package com.heng.util;

import com.heng.exception.ResponseCode;

//Ajax请求响应对象的类
public class AjaxResult {
    private boolean success = true;
    private String message = ResponseCode.RESPONSE_CODE_200.getMessage();
    private Integer code = ResponseCode.RESPONSE_CODE_200.getCode();


    //返回到前台对象
    private Object resultObj;

    public boolean isSuccess() {
        return success;
    }

    public AjaxResult setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public AjaxResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public AjaxResult setCode(Integer code) {
        this.code = code;
        return this;
    }

    public Object getResultObj() {
        return resultObj;
    }

    public AjaxResult setResultObj(Object resultObj) {
        this.resultObj = resultObj;
        return this;
    }

    //AjaxResult.me()成功
    //AjaxResult.me().setMessage()成功
    //AjaxResult.me().setSuccess(false),setMessage("失败");
    public  static AjaxResult me(){
        return new AjaxResult();
    }



    /*
    //成功
    public AjaxResult() {
    }

    //失败并且有提示
    public AjaxResult(String message) {
        this.success = false;
        this.message = message;
    }*/
}
