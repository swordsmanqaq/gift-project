package com.heng.dto;/**
 * @author shkstart
 * @create 2023-05-11 14:52
 */

import lombok.Data;

/**
 *@Auther:Jarvis
 *@Date:2023年05月2023/5/11日14:52
 *@Description:
 */
@Data
public class RegisterDTO {

    private String phone;
    private String password;
    //短信验证码
    private String smsCode;
}
