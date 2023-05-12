package com.heng.service.impl;

import com.heng.domain.UserAddress;
import com.heng.mapper.UserAddressMapper;
import com.heng.service.IUserAddressService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 收货地址 服务实现类
 * </p>
 *
 * @author Jarvis-Smith
 * @since 2023-05-11
 */
@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements IUserAddressService {

}
