package com.heng.constants;

//系统常量
public class BaseConstants {

    /**
     * 短信常量类
     */
    public class SmsContants {
        //用户名
        public static final String UID = "JarvisSmith";
        //秘钥
        public static final String KEY = "43A293807A8E0FF7A347D6320A56E709";
    }

    /**
     * 手机验证码存入redis key的前缀
     */
    public class MessageCode {
        //注册
        public static final String REGISTER_SMS_CODE = "register";
        //登录
        public static final String LOGIN_SMS_CODE = "login";
        //修改密码
        public static final String CHANGEPASSWORD_SMS_CODE = "changepassword";
        //绑定微信
        public static final String BINDWECHAT_SMS_CODE = "bindwechat";
    }

}