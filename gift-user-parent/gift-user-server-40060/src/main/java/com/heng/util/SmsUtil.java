package com.heng.util;


import com.heng.constants.BaseConstants;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * 短信发送工具类
 */
public class SmsUtil {

    /**
     * 发送短信
     * @param phones 手机们 a,b
     * @param content 发送内容
     * @return 返回值
     */
    public static String  sendSms(String phones,String content){
        PostMethod post = null;
        try {
            HttpClient client = new HttpClient();
            post = new PostMethod("http://utf8.api.smschinese.cn");
            post.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=utf8");//在头文件中设置转码
            NameValuePair[] data ={ new NameValuePair("Uid", BaseConstants.SmsContants.UID),
                    new NameValuePair("Key", BaseConstants.SmsContants.KEY),
                    new NameValuePair("smsMob",phones),
                    new NameValuePair("smsText",content)};
            post.setRequestBody(data);

            client.executeMethod(post);
            int statusCode = post.getStatusCode();
            System.out.println("statusCode:"+statusCode); //200 404 400
            String result = new String(post.getResponseBodyAsString().getBytes("utf8"));
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (post != null) {

                post.releaseConnection();
            }
        }
        return null;
    }

//    public static void main(String[] args) {
//        System.out.println(SmsUtil
//                .sendSms("13532771648", "您的验证码为：8081"));
//    }

}