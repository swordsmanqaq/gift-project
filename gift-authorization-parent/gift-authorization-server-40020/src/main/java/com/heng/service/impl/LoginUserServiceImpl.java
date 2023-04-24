package com.heng.service.impl;

import com.heng.domain.LoginUser;
import com.heng.mapper.LoginUserMapper;
import com.heng.service.ILoginUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jarvis-Smith
 * @since 2023-04-24
 */
@Service
public class LoginUserServiceImpl extends ServiceImpl<LoginUserMapper, LoginUser> implements ILoginUserService {

}
