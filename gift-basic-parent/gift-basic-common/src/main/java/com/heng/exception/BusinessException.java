package com.heng.exception;/**
 * @author shkstart
 * @create 2023-04-25 18:44
 */

/**
 *@Auther:Jarvis
 *@Date:2023年04月2023/4/25日18:44
 *@Description: 自定义异常类 继承RuntimeException
 */
public class BusinessException extends RuntimeException{

    private Integer code;

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message, Throwable cause, Integer code) {
        super(message, cause);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    protected BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {

    }
}
