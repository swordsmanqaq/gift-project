package com.heng.service.impl;

import com.heng.domain.UserInfo;
import com.heng.mapper.UserInfoMapper;
import com.heng.service.IUserInfoService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员基本信息 服务实现类
 * </p>
 *
 * @author Jarvis-Smith
 * @since 2023-05-11
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

}
