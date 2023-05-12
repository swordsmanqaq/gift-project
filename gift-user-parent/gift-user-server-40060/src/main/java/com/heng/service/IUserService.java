package com.heng.service;

import com.heng.domain.User;
import com.baomidou.mybatisplus.service.IService;
import com.heng.dto.RegisterDTO;

/**
 * <p>
 * 会员登录账号 服务类
 * </p>
 *
 * @author Jarvis-Smith
 * @since 2023-05-11
 */
public interface IUserService extends IService<User> {

    //手机注册
    void phoneRegister(RegisterDTO registerDTO);
    void sendRegisterMessageCode(String phone);

}
