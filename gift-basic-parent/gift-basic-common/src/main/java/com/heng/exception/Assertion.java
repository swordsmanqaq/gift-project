package com.heng.exception;/**
 * @author shkstart
 * @create 2023-04-25 18:49
 */

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

/**
 *@Auther:Jarvis
 *@Date:2023年04月2023/4/25日18:49
 *@Description: 断言类
 */
public class Assertion {

    /**
     * 断言字符串是空
     * @param str
     * @param message
     */
    public static void isBlank(String str, String message) {
        if (!StringUtils.isBlank(str)){
            throw new BusinessException(message);
        }
    }

    /**
     * 断言字符串不是空
     * @param str
     * @param message
     */
    public static void isNotBlank(String str, String message) {
        if (StringUtils.isBlank(str)){
            throw new BusinessException(message);
        }
    }

    /**
     * 断言对象为空
     * @param obj
     * @param message
     */
    public static void isNull(Object obj, String message){
        if (obj != null)
            throw new BusinessException(message);
    }

    /**
     * 断言对象不为空
     * @param obj
     * @param message
     */
    public static void isNotNull(Object obj, String message){
        if (obj == null)
            throw new BusinessException(message);
    }

    /**
     * 断言集合为空
     * @param objs
     * @param message
     */
    public static void isNull(Collection objs, String message){
        if (!(objs==null || objs.size() < 1))
            throw new BusinessException(message);
    }

    /**
     * 断言集合不为空
     * @param objs
     * @param message
     */
    public static void isNotNull(Collection objs, String message){
        if (objs==null || objs.size() < 1)
            throw new BusinessException(message);
    }

    /**
     * 断言为真值
     * @param exp
     * @param message
     */
    public static void isTrue(boolean exp, String message){
        if (!exp)
            throw new BusinessException(message);
    }

    /**
     * 断言为假值
     * @param exp
     * @param message
     */
    public static void isFalse(boolean exp, String message){
        if (exp)
            throw new BusinessException(message);
    }

    /**
     * 断言字符串相等
     * @param str
     * @param str1
     * @param message
     */
    public static void isEquals(String str, String str1 , String message){
        if (!str.equals(str1))
            throw new BusinessException(message);
    }

    /* 带有响应码的断言方法 */

    /**
     * 断言字符串是空
     * @param str
     * @param responseCode
     */
    public static void isBlank(String str, ResponseCode responseCode) {
        if (!StringUtils.isBlank(str)){
            throw new BusinessException(responseCode.getMessage(), responseCode.getCode());
        }
    }

    /**
     * 断言字符串不是空
     * @param str
     * @param responseCode
     */
    public static void isNotBlank(String str, ResponseCode responseCode) {
        if (StringUtils.isBlank(str)){
            throw new BusinessException(responseCode.getMessage(), responseCode.getCode());
        }
    }

    /**
     * 断言对象为空
     * @param obj
     * @param responseCode
     */
    public static void isNull(Object obj, ResponseCode responseCode){
        if (obj != null)
            throw new BusinessException(responseCode.getMessage(), responseCode.getCode());
    }

    /**
     * 断言对象不为空
     * @param obj
     * @param responseCode
     */
    public static void isNotNull(Object obj, ResponseCode responseCode){
        if (obj == null)
            throw new BusinessException(responseCode.getMessage(), responseCode.getCode());
    }

    /**
     * 断言集合为空
     * @param objs
     * @param responseCode
     */
    public static void isNull(Collection objs, ResponseCode responseCode){
        if (!(objs==null || objs.size() < 1))
            throw new BusinessException(responseCode.getMessage(), responseCode.getCode());
    }

    /**
     * 断言集合不为空
     * @param objs
     * @param responseCode
     */
    public static void isNotNull(Collection objs, ResponseCode responseCode){
        if (objs==null || objs.size() < 1)
            throw new BusinessException(responseCode.getMessage(), responseCode.getCode());
    }

    /**
     * 断言为真值
     * @param exp
     * @param responseCode
     */
    public static void isTrue(boolean exp, ResponseCode responseCode){
        if (!exp)
            throw new BusinessException(responseCode.getMessage(), responseCode.getCode());
    }

    /**
     * 断言为假值
     * @param exp
     * @param responseCode
     */
    public static void isFalse(boolean exp, ResponseCode responseCode){
        if (exp)
            throw new BusinessException(responseCode.getMessage(), responseCode.getCode());
    }

    /**
     * 断言字符串相等
     * @param str
     * @param str1
     * @param responseCode
     */
    public static void isEquals(String str, String str1 , ResponseCode responseCode){
        if (!str.equals(str1))
            throw new BusinessException(responseCode.getMessage(), responseCode.getCode());
    }
}
